<template>
  <div class="overlay dark-text">
    <div class="modal-container">
      <div class="modal-content">
        <span class="close" @click="closeModalAndGoBack">&times;</span>
        <div class="text-center mb-4">
          <h1 class="fs-3">Είσοδος</h1>
        </div>
        <div class="mb-2" v-if="authenticationFailed">
          <div class="alert alert-danger" role="alert">
            Αποτυχία Αυθεντικοποιήσης
          </div>
        </div>
        <div class="mb-2">
          <label for="usernameFormControl" class="form-label mb-1">Όνομα Χρήστη</label>
          <div class="col-sm-4">
            <input v-model="credentials.username" type="text" class="form-control" id="usernameFormControl" />
          </div>
        </div>
        <div class="mb-2">
          <label for="passwordFormControl" class="form-label mb-1">Κωδικός Πρόσβασης</label>
          <div class="col-sm-4">
            <input v-model="credentials.password" type="password" class="form-control" id="passwordFormControl" />
          </div>
        </div>
        <div>
          <button type="button" class="btn btn-primary btn-lg rounded-3 font-bold" @click.prevent="onFormSubmit">Περάστε!</button>
        </div>
        <div>
          Όλοι οι καλοί χωράνε! <a href="/register">Εγγραφείτε</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useApplicationStore } from '@/stores/application.js';

const router = useRouter();
const showModal = ref(true);
const {loadUserData, setUserData, persistUserData, isAuthenticated } = useApplicationStore();

const loading = ref(false);
const credentials = ref({
  username: '',
  password: ''
});
const authenticationFailed = ref(false);


const onFormSubmit = () => {
  loading.value = true;
  authenticationFailed.value = false;

  fetch(`http://localhost:8080/api/auth/signin`, { // Use backendURL variable here
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials.value)
  })
      .then((response) => {
        if (response.ok) {
          response.json().then((data) => {
            setUserData(data);
            persistUserData();
            router.push({name: 'home'});
          });
        } else {
          authenticationFailed.value = true;
        }
      })
      .catch((err) => {
        console.warn(err);
        authenticationFailed.value = true;
      })
      .finally(() => {
        loading.value = false;
      });
};

onBeforeMount(() => {
  if (isAuthenticated.value === true) {
    router.replace({name: 'home'});
  }
});

const closeModal = () => {
  showModal.value = false;
};

const goBack = () => {
  if (window.confirm("Είστε σίγουρος ότι θέλετε να επιστρέψετε;")) {
    window.history.back();
  }
};

const closeModalAndGoBack = () => {
  closeModal();
  setTimeout(() => {
    goBack();
  }, 300);
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background-color: #9E6871;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
  text-align: center;
}

h1 {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 1rem;
}

label {
  font-size: 1rem;
  color: #333;
  margin-bottom: 0.5rem;
  display: block;
}

input.form-control {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  margin-bottom: 1rem;
  width: 80%;
  align-content: center;
  font-size: 1rem;
}

input.form-control:focus {
  border-color: #a86f72;
  outline: none;
  box-shadow: 0 0 5px rgba(52, 152, 219, 0.5);
}

button.btn {
  background-color: #80535A;
  color: white;
  border: none;
  padding: 12px 24px;
  font-size: 1.1rem;
  border-radius: 5px;
  cursor: pointer;
  width: 50%;
  transition: background-color 0.3s ease;
}

button.btn:hover {
  background-color: #a86f72;
}

a {
  color: #3498db;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

.alert.alert-danger {
  background-color: #e74c3c;
  color: white;
  padding: 1rem;
  border-radius: 5px;
  margin-bottom: 1rem;
  font-size: 1rem;
}
</style>