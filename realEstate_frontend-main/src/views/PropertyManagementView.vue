<template>
  <div class="properties-list-view white-text">
    <div v-if="properties && properties.length > 0">
      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
        </tr>
        <tr>
          <th>City</th>
        </tr>
        <tr>
          <th>Street</th>
        </tr>
        <tr>
          <th>Street Number</th>
        </tr>
        <tr>
          <th>Price</th>
        </tr>
        <tr>
          <th>Size</th>
        </tr>
        <tr>
          <th>Number of Rooms</th>
        </tr>
        <tr>
          <th>Number of Bathrooms</th>
        </tr>
        <tr>
          <th>Number of Bedrooms</th>
        </tr>
        <tr>
          <th>Furnished</th>
        </tr>
        <tr>
          <th>Parking</th>
        </tr>
        <tr>
          <th>Balcony</th>
        </tr>
        <tr>
          <th>Garden</th>
        </tr>
        <tr>
          <th>Heating</th>
        </tr>
        <tr>
          <th>AC</th>
        </tr>
        <tr>
          <th>Pets Allowed</th>
        </tr>
        <tr>
          <th>Built Year</th>
        </tr>
        <tr>
          <th>Renovation Year</th>
        </tr>
        <tr>
          <th>Status</th>
        </tr>
        </thead>
        <tbody>

        <tr v-for="property in paginatedProperties" :key="property.id">
          <td>{{ property.id }}</td>
          <td>{{ property.city }}</td>
          <td>{{ property.street }}</td>
          <td>{{ property.streetNum }}</td>
          <td>{{ property.price }}</td>
          <td>{{ property.size }}</td>
          <td>{{ property.roomNum }}</td>
          <td>{{ property.bathNum }}</td>
          <td>{{ property.bedNum }}</td>
          <td>{{ property.isFurnished }}</td>
          <td>{{ property.hasParking }}</td>
          <td>{{ property.hasBalcony }}</td>
          <td>{{ property.hasGarden }}</td>
          <td>{{ property.hasHeating }}</td>
          <td>{{ property.hasAC }}</td>
          <td>{{ property.petsAllowed }}</td>
          <td>{{ property.builtYear }}</td>
          <td>{{ property.renovationYear }}</td>
          <td>
            <!-- Show buttons only if the request is a rental request or if the status is null for viewing requests -->
            <button v-if=" (property.isApproved === false)" class="btn stylish-btn" @click="approveProperty(property.id)">
              Approve Property
            </button>

            <button v-if=" (property.isApproved === false) " class="btn stylish-btn" @click="declineProperty(property.id)">
              Decline Property
            </button>
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination white-text"> <!-- buttons for previous and next pages in the table -->
        <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
        Page {{ currentPage }} of {{ totalPages }}
        <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
      </div>
    </div>
    <div v-else>
      <h3>No Properties</h3>
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

import {useApplicationStore} from "@/stores/application.js";
import {useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";

const { loadUserData } = useApplicationStore();
const userData = loadUserData();
const router = useRouter();

const properties = ref([]);
const itemsPerPage = 5;
const currentPage = ref(1);
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

onMounted(() => {
  fetchProperties();
});

const fetchProperties = () => {
  fetch(`http://localhost:8080/api/admin/getNonApprovedProperties`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then(response => response.json())
      .then(data => {
        properties.value = data;
        console.log(requests.value);
      })
      .catch(error => console.error('Error fetching properties:', error));
};

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

const approveProperty = (propertyId) => {
  fetch(`http://localhost:8080/api/admin//approveProperty/${propertyid}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Property approved successfully!');
          fetchProperties();
        } else {
          openModal('Failed to approve the property.');
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};

const declineProperty = (requestId) => {
  fetch(`http://localhost:8080/api/admin/deleteProperty/${propertyid}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then((response) => {
        if (response.ok) {
          openModal('Property declined successfully!');
          fetchProperties();
        } else {
          openModal('Failed to decline the property.');
        }
      })
      .catch((error) => {
        openModal(`Error: ${error}`);
      });
};
</script>

<style scoped>

</style>