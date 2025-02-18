<template>
    <h1>Το Προφίλ μου</h1>
    <div class="user-profile">
        <div class="user-info">
            <span class="label">Όνομα Χρήστη: </span> <span>{{ user?.user?.username || 'N/A' }}</span>
        </div>
        <div class="user-info">
            <span class="label">Email: </span> <span>{{ user?.email || 'N/A' }}</span>
        </div>
        <div class="user-info">
            <span class="label">Όνομα: </span> <span>{{ user?.fname || 'N/A' }}</span>
        </div>
        <div class="user-info">
            <span class="label">Επίθετο: </span> <span>{{ user?.lname || 'N/A' }}</span>
        </div>
        <div class="user-info">
            <span class="label">Ενεργοποιημένος: </span> 
            <span v-if="user?.user?.isApproved">✔️</span>
            <span v-else>✘</span>
        </div>
        <div class="user-info">
            <span class="label">Ρόλοι: </span>
            <span v-if="user?.user?.roles?.some(role => role.name === 'ROLE_TENANT')">Ενοικιαστής</span>
            <span v-else-if="user?.user?.roles?.some(role => role.name === 'ROLE_OWNER')">Ιδιοκτήτης</span>
            <span v-else-if="user?.user?.roles?.some(role => role.name === 'ROLE_ADMIN')">Administrator</span>
            <span v-else>Χωρίς ρόλο</span>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useApplicationStore } from '@/stores/application.js';

const { loadUserData } = useApplicationStore();
const userData = loadUserData();

const user = ref([]);
const itemsPerPage = 5;
const currentPage = ref(1);
const showModal = ref(false);
const modalMessage = ref('');

onMounted(() => {
  fetch(`http://localhost:8080/api/owner/getMyProfile`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
  })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch user');
        }
        return response.json();
      })
      .then(data => {
        user.value = data;
      })
      .catch(error => {
        console.error('Error fetching user:', error);
      });
});

const removeUser = (userId) => {
  fetch(`http://localhost:8080/api/admin/declineUser/${userId}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    }
  })
      .then(response => {
        if (response.ok) {
          openModal('Removed successfully!');
        } else {
          throw new Error('Failed to remove');
        }
        return response.json();
      })
      .catch(error => {
        console.error('Error removing user:', error);
      });
};

const paginatedUsers = computed(() => {
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
.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.user-card {
  width: 100%;
  max-width: 400px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  border: 1px solid #ccc;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.user-info {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  color: #333;
  padding: 5px 0;
  border-bottom: 1px solid #eee;
}

.label {
  font-weight: bold;
  color: #535A80;
}

button {
  margin-top: 10px;
  background: #7c0b0b;
  color: white;
  font-size: 16px;
  padding: 10px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.1s ease-in-out;
  width: 100%;
}

button:hover {
  background: #df585e;
  box-shadow: 0 0 12px #df585e;
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}
</style>