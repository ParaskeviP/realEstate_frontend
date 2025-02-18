<template>
  <div>
    <div v-if="paginatedProperties.length > 0">
    <h1>Εκκρεμείς Αιτήσεις</h1>
      <table class="table">
        <thead>
        <tr>
            <th>ID Ιδιοκτήτη</th>
            <th>ID Ακινήτου</th>
            <th>Πόλη</th>
            <th>Οδός</th>
            <th>Νούμερο</th>
            <th>Μέγεθος (τ.μ)</th>
            <th>Τιμή (€)</th>
            <th>Αριθμός Δωματίων</th>
            <th>Αριθμός Υπνοδωματίων</th>
            <th>Αριθμός WC</th>
            <th>Επιλογές</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="property in paginatedProperties" :key="property.id">
          <td>{{ property.owner.id }}</td>
          <td>{{ property.id }}</td>
          <td>{{ property.city }}</td>
          <td>{{ property.street }}</td>
          <td>{{ property.streetNum }}</td>
          <td>{{ property.size }}</td>
          <td>{{ property.price }}</td>
          <td>{{ property.roomNum }}</td>
          <td>{{ property.bedNum }}</td>
          <td>{{ property.bathNum }}</td>
          <td v-if="property.id">
            <button class="btn stylish-btn" @click="removeProperty(property.id)">Απόρριψη</button>
            <button class="btn stylish-btn" @click="approveProperty(property.id)">Επικύρωση</button>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1" class="pagination-btn"><=</button>
        Σελίδα {{ currentPage }}/{{ totalPages }}
        <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-btn">=></button>
      </div>
    </div>
    <div v-else>
      Δεν εκκρεμούν αιτήσεις!
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

const { loadUserData } = useApplicationStore();
const userData = loadUserData();

const users = ref([]);
const itemsPerPage = 5;
const currentPage = ref(1);
const showModal = ref(false);
const modalMessage = ref('');

onMounted(() => {
  fetch(`http://localhost:8080/api/admin/getNonApprovedProperties`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch properties');
        }
        return response.json();
      })
      .then(data => {
        users.value = data;
      })
      .catch(error => {
        console.error('Error fetching properties:', error);
      });

});

const removeProperty = (propertyId) => {
  fetch(`http://localhost:8080/api/admin/deleteProperty/${propertyId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    }
  })
      .then(response => {
        if (response.ok) {
          openModal('Απερρίφθη Επιτυχώς!');
        } else {
          throw new Error('Αποτυχία απόρρηψης.');
        }
        return response.json();
      })
      .catch(error => {
        console.error('Η απόρριψη δεν επετεύχθει:', error);
      });
};

const approveProperty = (propertyId) => {
  fetch(`http://localhost:8080/api/admin/approveProperty/${propertyId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    }
  })
      .then(response => {
        if (response.ok) {
          openModal('Επιτυχία!');
        } else {
          throw new Error('Αποτύχια Έγκρισης.');
        }
        return response.json();
      })
      .catch(error => {
        console.error('Η έγκριση δεν επετεύχθει:', error);
      });
};

const paginatedProperties = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return users.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(users.value.length / itemsPerPage));

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

</script>

<style>
.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table {
  width: 70%;
  border-collapse: collapse;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #ccc;
}

table {
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  margin: auto;
}

th, td {
  padding: 0px 10px;
  text-align: center;
  font-size: 16px;
  color: #333;
}

th {
  background-color: #535A80;
  color: white;
  font-weight: bold;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f1f1f157;
  cursor: pointer;
  border-radius: 10px;
}

td {
  font-family: 'Arial', sans-serif;
}

table th,
table td {
  transition: all 0.7s ease-in-out;
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

button:hover {
  background: linear-gradient(45deg, #535A80, #003f7f);
  transform: translateY(-3px); 
  box-shadow: 0 6px 12px rgba(0, 86, 179, 0.4);
}

button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 86, 179, 0.4);
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
  background-color: rgba(255, 255, 255, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background-color: #ffffff;
  color: #333333;
  margin: 0 auto;
  padding: 20px;
  border-radius: 5px;
  border: 1px solid #333333;
  width: 60%;
  max-width: 400px;
  z-index: 1002;
}

.close {
  color: #333333;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: #aaaaaa;
  text-decoration: none;
}

.success {
  color: #333333;
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}
</style>