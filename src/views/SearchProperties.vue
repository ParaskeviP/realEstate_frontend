<template>
  <div class="property-search">
    <h1>Αναζήτηση</h1>
    <form @submit.prevent="searchProperties">
      <!-- Επιλογή Τύπου Αναζήτησης -->
      <div>
        <label for="searchType">Εφαρμογή Φίλτρου</label>
        <select v-model="searchType" id="searchType" required>
          <option value="getByRoomNumber">Αριθμός Δωματίων</option>
          <option value="getByBedNumber">Αριθμός Υπνοδωματίων</option>
          <option value="getByBathNumber">Αριθμός WC</option>
          <option value="getByCity">Πόλη</option>
          <option value="getByStreet">Οδός</option>
          <option value="getBySize">Μέγεθος (τ.μ)</option>
          <option value="getByPrice">Τιμή (€)</option>
          <option value="getByPetsAllowed">Επιτρέπονται τα κατοικίδια</option>
          <option value="getByHasGarden">Έχει Κήπο</option>
          <option value="getByHasBalcony">Έχει Μπαλκόνια</option>
          <option value="getByHasHeating">Έχει Κεντρική Θέρμανση</option>
          <option value="getByHasAC">Έχει Ψήξη</option>
          <option value="getByIsFurnished">Είναι Επιπλωμένο</option>
          <option value="getByBuiltYear">Έτος Κατασκευής</option>
          <option value="getByRenovYear">Έτος Ανακαίνησης</option>
        </select>
      </div>

      <!-- Επιλογή Τιμής -->
      <label for="queryValue">Τιμή</label>

      <!-- Dropdown για Ναι/Όχι -->
      <select
          v-if="['getByPetsAllowed', 'getByHasGarden', 'getByHasBalcony', 'getByHasHeating', 'getByHasAC', 'getByIsFurnished'].includes(searchType)"
          v-model="queryValue" id="queryValue" required>
        <option :value="true">Ναι</option>
        <option :value="false">Όχι</option>
      </select>

      <!-- Input για πληκτρολόγηση -->
      <input
          v-else v-model="queryValue" type="text" id="queryValue" placeholder="Enter value" required/>

      <!-- Εμφάνιση Αποτελεσμάτων -->
      <button type="submit">Αναζήτηση</button>
    </form>

    <!-- Αποτελέσματα Αναζήτησης -->
    <div v-if="properties.length > 0">
      <h2>Αποτελέσματα Αναζήτησης</h2>
        <div style="overflow-x:auto;">
          <table>
            <thead>
            <tr>
              <th>Πόλη</th>
              <th>Οδός</th>
              <th>Νούμερο</th>
              <th>Μέγεθος (τ.μ)</th>
              <th>Τιμή (€)</th>
              <th>Αριθμός Δωματίων</th>
              <th>Αριθμός Υπνοδωματίων</th>
              <th>Αριθμός WC</th>
              <th>Επιτρέπονται τα κατοικίδια</th>
              <th>Κήπο</th>
              <th>Μπαλκόνια</th>
              <th>Κεντρική Θέρμανση</th>
              <th>Ψήξη</th>
              <th>Επιπλωμένο</th>
              <th>Έτος Κατασκευής</th>
              <th>Έτος Ανακαίνησης</th>
              <th>Επιλογές</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="property in paginatedProperties" :key="property.id">
              <td>{{ property.city }}</td>
              <td>{{ property.street }}</td>
              <td>{{ property.streetNum }}</td>
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
                <span v-if="property.hasAC">✔️</span>
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
        </div>
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1" class="pagination-btn"><=</button>
        Σελίδα {{ currentPage }}/{{ totalPages }}
        <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-btn">=></button>
      </div>
    </div>
    <div v-else-if="hasSearched">
      <h3>Δεν βρέθηκαν ακίνητα για την συγκεκριμένη αναζήτηση!</h3>
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
import { ref, onMounted, getCurrentInstance, computed} from 'vue';
import { useApplicationStore } from '@/stores/application.js';
const { loadUserData } = useApplicationStore();
const userData = loadUserData();
// Αρχικοποίηση των μεταβλητών
const searchType = ref("");
const queryValue = ref("");
const properties = ref([]);
const errorMessage = ref("");

const instance = getCurrentInstance();
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

const fetchProperties = () => {
  hasSearched.value = false; // Αρχικοποίηση

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
        hasSearched.value = true; // Σηματοδοτούμε ότι έγινε αναζήτηση
      })
      .catch(error => console.error('Error fetching properties:', error));

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
          openModal('Ευχαριστούμε! Ο Ιδιοκτήτης θα ειδοποιηθεί για το αίτημα σας!');
          fetchProperties();
        } else {
          response.json().then(data => {
            openModal(`Failed to submit the request: ${data.message || 'Unknown error'}`);
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
          openModal('Ευχαριστούμε! Ο Ιδιοκτήτης θα ειδοποιηθεί για το αίτημα σας!');
          fetchProperties();
        } else {
          response.json().then(data => {
            openModal(`Failed to submit the request: ${data.message || 'Unknown error'}`);
          });
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};

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

// Μέθοδος για αναζήτηση
async function searchProperties() {
  try {
    hasSearched.value = false; // Αρχικοποίηση
    const response = await fetch(
        `http://localhost:8080/api/properties/${searchType.value}/${queryValue.value}`,

        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${userData.token}`,},
        }
    );
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    hasSearched.value = true; // Σηματοδοτούμε ότι έγινε αναζήτηση

    const data = await response.json();
    properties.value = data;
    console.log(properties.value);
    errorMessage.value = ""; // Καθαρισμός μηνύματος σφάλματος
  } catch (error) {
    hasSearched.value = true; // Σηματοδοτούμε ότι έγινε αναζήτηση
    console.error("Error fetching properties:", error);
    errorMessage.value = "Failed to fetch properties. Please try again.";
  }
}

</script>

<style scoped>
.property-search select {
  width: 78%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #333;
  outline: none;
  transition: all 0.3s ease;
}

.property-search select:focus {
  border-color: #46482d;
}

.property-search label {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
  font-weight: bold;
  display: block;
  margin-top: 10px;
}

.property-search {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  border-radius: 10px;
}

.property-search input[type="text"] {
  width: 70%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  outline: none;
  transition: 0.3s;
}

.property-search input[type="text"]:focus {
  border-color: #46482d;
  box-shadow: 0 0 5px rgba(70, 72, 45, 0.5);
}

.error {
  color: red;
  font-weight: bold;
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