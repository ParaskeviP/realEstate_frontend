<template>
  <div class="property-search">
    <h1>Αναζήτηση</h1>
    <form @submit.prevent="searchProperties">
      <div class="filter-box">
        <label for="searchType">Εφαρμογή Φίλτρου</label>
          <input v-model="roomNumFilter" placeholder="Αριθμός Δωματίων" type="number">
          <input v-model="bedNumFilter" placeholder="Αριθμός Κρεβατοκαμαρών" type="number">
          <input v-model="bathNumFilter" placeholder="Αριθμός WC" type="number">
          <input v-model="cityFilter" placeholder="Πόλη" type="text">
          <input v-model="sizeFilter" placeholder="Μέγεθος (m²)" type="number">
          <input v-model="priceFilter" placeholder="Μέγιστη Τιμή" type="number">
          <label><input v-model="petsAllowedFilter" type="checkbox">Επιτρέπονται Κατοικίδια</label>
          <label><input v-model="hasGardenFilter" type="checkbox">Έχει Κήπο</label>
          <label><input v-model="hasBalconyFilter" type="checkbox">Έχει Μπαλκόνια</label>
          <label><input v-model="hasHeatingFilter" type="checkbox">Έχει Θέρμανση</label>
          <label><input v-model="isFurnishedFilter" type="checkbox">Είναι Επιπλωμένο</label>

          <button type="submit">🔎</button>
      </div>
    </form>

    <!-- Αποτελέσματα Αναζήτησης -->
    <div v-if="properties.length > 0">
      <h2>Αποτελέσματα Αναζήτησης</h2>
        <div style="overflow-x:auto;">
          <table>
            <thead>
            <tr>
              <th>ID</th>
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
              <td>{{ property.id }}</td>
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

// const fetchProperties = () => {
//   hasSearched.value = false; // Αρχικοποίηση

//   fetch(`http://localhost:8080/api/tenant/showProperties`, {  //only properties without tenant
//     method: 'GET',
//     headers: {
//       'Content-Type': 'application/json',
//       'Authorization': `Bearer ${userData.token}`,
//     },
//   })
//       .then(response => response.json())
//       .then(data => {
//         properties.value = data;
//         hasSearched.value = true; // Σηματοδοτούμε ότι έγινε αναζήτηση
//       })
//       .catch(error => console.error('Error fetching properties:', error));

// };

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

const params = new URLSearchParams();
const roomNumFilter=ref("");
const bedNumFilter=ref("");
const bathNumFilter=ref("");
const cityFilter=ref("");
const sizeFilter=ref("");
const priceFilter=ref("");
const petsAllowedFilter=ref("");
const hasGardenFilter=ref("");
const hasBalconyFilter=ref("");
const hasHeatingFilter=ref("");
const isFurnishedFilter=ref("");

async function searchProperties() {
  try {
      hasSearched.value = false;

      console.log(roomNumFilter.value);

      if(roomNumFilter.value) params.append("roomNum", roomNumFilter.value);
      if(bedNumFilter.value) params.append("bedNum", bedNumFilter.value);
      if(bathNumFilter.value) params.append("bathNum", bathNumFilter.value);
      if(cityFilter.value) params.append("city", cityFilter.value);
      if(sizeFilter.value) params.append("size", sizeFilter.value);
      if(priceFilter.value) params.append("price", priceFilter.value);
      if(petsAllowedFilter.value !== undefined) params.append("petsAllowed", petsAllowedFilter.value);
      if(hasGardenFilter.value !== undefined) params.append("hasGarden", hasGardenFilter.value);
      if(hasBalconyFilter.value !== undefined) params.append("hasBalcony", hasBalconyFilter.value);
      if(hasHeatingFilter.value !== undefined) params.append("hasHeating", hasHeatingFilter.value);
      if(isFurnishedFilter.value !== undefined) params.append("isFurnished", isFurnishedFilter.value);

      console.log(params.toString());

      const filterUrl=`http://localhost:8080/api/properties/search?${params.toString()}`;

      const response = await fetch(
        filterUrl,
            {
              method: "GET",
              headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${userData.token}`
            }
          }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      hasSearched.value = true;

      const data = await response.json();
      properties.value = data;
      errorMessage.value = "";
  } catch (error) {
      hasSearched.value = true;
      console.error("Error fetching properties:", error);
      errorMessage.value = "Failed to fetch properties. Please try again.";
  }
}

</script>

<style scoped>
.filter-box {
  position: fixed;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 250px;
  background: #ddd;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  overflow-y: auto;
}

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

.property-search input[type="number"] {
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

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}
</style> 