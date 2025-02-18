<template>
  <h1>Εκκρεμούσες Αιτήσεις</h1>
  <div class="request-list-view white-text">
    <div v-if="paginatedRequests.length>0">
      <table class="table">
        <thead>
        <tr>
          <th>ID Ενοικιαστή</th>
          <th>ID Ακινήτου</th>
          <th>Κατηγορία Αιτήματος</th>
          <th>Κατάσταση</th>
          <th>Επιλογές</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="request in paginatedRequests" :key="request.id">
          <td>{{ request.tenant.id }}</td>
          <td>{{ request.property.id }}</td>
          <td>{{ request.isRentalRequest ? "Ενοικίαση" : "Επίδειξη" }}</td>
          <td>
            {{ request.isViewingApproved === null ? "Εκκρεμεί" : request.isViewingApproved ? "Δεκτό" : "Απερρίφθη" }}
          </td>

          <td>
            <!-- Show buttons only if the request is a rental request or if the status is null for viewing requests -->
            <button v-if=" (request.isViewingApproved ===null && !request.isViewingApproved === true && !request.isViewingApproved === false) || request.isRentalRequest ||( !request.isRentalRequest && request.isViewingApproved === null)" class="btn stylish-btn" @click="approveRequest(request.id, request.isRentalRequest)">
              Έγκριση
            </button>

            <button v-if=" (request.isViewingApproved ===null && !request.isViewingApproved === true && !request.isViewingApproved === false) || request.isRentalRequest ||( !request.isRentalRequest && request.isViewingApproved === null)" class="btn stylish-btn" @click="declineRequest(request.id)">
              Απόρριψη
            </button>
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination white-text"> <!-- buttons for previous and next pages in the table -->
        <button @click="prevPage" :disabled="currentPage === 1"><=</button>
        Σελίδα {{ currentPage }}/{{ totalPages }}
        <button @click="nextPage" :disabled="currentPage === totalPages">=></button>
      </div>
    </div>
    <div v-else-if="hasSearched">
      <h3>No Requests</h3>
    </div>

    <div v-if="showModal" class="modal">
      <div class="modal-content success">
        <span class="close" @click="closeModal">&times;</span>
        <p>{{ modalMessage }}</p>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useApplicationStore } from '@/stores/application.js';
import { useRouter } from 'vue-router';

const { loadUserData } = useApplicationStore();
const userData = loadUserData();
const router = useRouter();

const requests = ref([]);
const itemsPerPage = 5;
const currentPage = ref(1);
const showModal = ref(false);
const modalMessage = ref('');
const hasSearched = ref(false); // Μεταβλητή για να ελέγχουμε αν έγινε αναζήτηση

const openModal = (message) => {
  modalMessage.value = message;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  setTimeout(() => {
    location.reload();
  }, 500);
};

onMounted(() => {
  fetchRequests();
});

const fetchRequests = () => {
  hasSearched.value = false; // Αρχικοποίηση
  fetch(`http://localhost:8080/api/owner/getRequests`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then(response => response.json())
      .then(data => {
        requests.value = data;
        hasSearched.value = true; // Σηματοδοτούμε ότι έγινε αναζήτηση
        console.log(requests.value);
      })
      .catch(error => console.error('Error fetching requests:', error));
};

const paginatedRequests = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return requests.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(requests.value.length / itemsPerPage));

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const approveRequest = (requestId, isRentalRequest) => {
  if (isRentalRequest) {
    approveRentalRequest(requestId);
  } else {
    approveViewingRequest(requestId);
  }
};

const approveViewingRequest = (requestId) => {
  fetch(`http://localhost:8080/api/owner/approveViewingRequest/${requestId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Request approved successfully!');
          fetchRequests();
        } else {
          response.json().then(data => {
            openModal(`Failed to approve the request: ${data.message || 'Unknown error'}`);
          });
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};


const approveRentalRequest = (requestId) => {
  fetch(`http://localhost:8080/api/owner/approveRentalRequest/${requestId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Request approved successfully!');
          fetchRequests();
        } else {
          openModal('Failed to approve the request.');
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};

const declineRequest = (requestId) => {
  fetch(`http://localhost:8080/api/owner/deleteRequest/${requestId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Request declined successfully!');
          fetchRequests();
        } else {
          openModal('Failed to decline the request.');
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};
</script>


<style scoped>
.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination button {
  background-color: #3498db; /* Blue */
  color: #fff; /* White */
  padding: 8px 16px;
  cursor: pointer;
  border: none;
}

table th,
table td {
  transition: all 0.7s ease-in-out;
}

table {
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  margin: auto;
}

th {
  background-color: #535A80;
  color: white;
  font-weight: bold;
  padding: 10px;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f1f1f157;
  cursor: pointer;
  border-radius: 10px;
}

.request-list-view {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.modal {
  display: none;
  position: fixed;
  z-index: 1001;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background-color: #f0f0f0;
  color: #333;
  margin: 0 auto;
  padding: 20px;
  border-radius: 5px;
  border: 1px solid #333;
  width: 60%;
  max-width: 400px;
  z-index: 1002;
}

button {
  margin-top: 10px;
  background: #535A80;
  color: white;
  font-size: 16px;
  padding: 13px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.1s ease-in-out;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.3);
}

.close {
  color: #333; /* Dark gray */
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: #aaa; /* Lighter gray on hover/focus */
  text-decoration: none;
}

.success {
  color: #2ecc71; /* Green */
}


h3 {
  margin-top: 20px;
  color: #333; /* Dark gray */
}

.white-text {
  color: #333; /* Dark gray */
}

button:hover {
  background: linear-gradient(45deg, #535A80, #003f7f);
  transform: translateY(-3px); 
  box-shadow: 0 6px 12px rgba(0, 86, 179, 0.4);
}

button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 86, 179, 0.4);
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}
</style>

