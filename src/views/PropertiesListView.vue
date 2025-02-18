<template>
  <div>
    <h1>Προβολή Όλων</h1>
    <div v-if="paginatedProperties.length > 0">
      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Πόλη</th>
          <th>Μέγεθος</th>
          <th>Τιμή</th>
          <th>Αριθμός Δωματίων</th>
          <th>Αριθμός Υπνοδωματίων</th>
          <th>Αριθμός WC</th>
          <th>Επιτρέπονται τα κατοικίδια</th>
          <th>Έχει Κήπο</th>
          <th>Έχει Μπαλκόνια</th>
          <th>Έχει Κεντρική Θέρμανση</th>
          <th>Είναι Επιπλωμένο</th>
          <th>Έτος Κατασκευής</th>
          <th>Έτος Ανακαίνησης</th>
          <th>Επιλογές</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="property in paginatedProperties" :key="property.id">
          <td>{{ property.id }}</td>
          <td>{{ property.city }}</td>
          <td>{{ property.size }}</td>
          <td>{{ property.price }}</td>
          <td>{{ property.roomNum }}</td>
          <td>{{ property.bedNum }}</td>
          <td>{{ property.bathNum }}</td>
          <td>
            <span v-if="property.petsAllowed">✔️</span>
            <span v-else>✘</span>
          </td>
          <td>
            <span v-if="property.hasGarden">✔️</span>
            <span v-else>✘</span>
          </td>
          <td>
            <span v-if="property.hasBalcony">✔️</span>
            <span v-else>✘</span>
          </td>
          <td>
            <span v-if="property.hasHeating">✔️</span>
            <span v-else>✘</span>
          </td>
          <td>
            <span v-if="property.isFurnished">✔️</span>
            <span v-else>✘</span>
          </td>
          <td>{{ property.builtYear }}</td>
          <td>{{ property.renovationYear }}</td>
          <button class="btn stylish-btn" @click="makeRentalRequest(property.id)">
            Θέλω να Ενοικιάσω
          </button>
          <button class="btn stylish-btn" @click="makeViewingRequest(property.id)">
            Θέλω να το Δω
          </button>

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
      <h3>Δεν υπάρχουν διαθέσιμα Ακίνητα.</h3>
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
import { ref, onMounted, getCurrentInstance, computed } from 'vue';
import { useApplicationStore } from '@/stores/application.js';
const { loadUserData } = useApplicationStore();
const userData = loadUserData();
const properties = ref([]);
const instance = getCurrentInstance();
const showModal = ref(false);
const modalMessage = ref('');

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

const fetchProperties = () => {

  fetch(`http://localhost:8080/api/tenant/showProperties`, {  //only properties without tenant
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then(response => response.json())
      .then(data => {
        properties.value = data;
        console.log(properties.value);
      })
      .catch(error => console.error('Error fetching properties:', error));

};

// Κάλεσε την `fetchProperties` κατά την εκκίνηση
onMounted(() => {
  fetchProperties();
});


const itemsPerPage = 5;
const currentPage = ref(1);

const paginatedProperties = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return properties.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(properties.value.length / itemsPerPage));

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

const makeRentalRequest = (propertyId) => {
  fetch(`http://localhost:8080/api/tenant/makeRentalRequest/${propertyId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Η αίτηση υπεβλήθη επιτυχώς!');
          fetchProperties();
        } else {
          response.json().then(data => {
            openModal(`Αποτυχία υποβολής: ${data.message || 'Unknown error'}`);
          });
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};

const makeViewingRequest = (propertyId) => {
  fetch(`http://localhost:8080/api/tenant/makeViewingRequest/${propertyId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Η αίτηση υπεβλήθη επιτυχώς!');
          fetchProperties();
        } else {
          response.json().then(data => {
            openModal(`Αποτυχία υποβολής: ${data.message || 'Unknown error'}`);
          });
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};
</script>

<style>
.pos {
  margin-top: 20px;
}

.editBtn {
  margin-right: 5px;
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
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

.close {
  color: #333;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: #666;
  text-decoration: none;
}

.success {
  color: #333;
}

.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table {
  width: 100%;
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
</style>