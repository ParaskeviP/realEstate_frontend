<template>
  <div class="overlay white-text" ref="overlay" @click.self="closeModal">
    <div class="modal-container">
      <div class="modal-content">
        <span class="close" @click="closeModalAndGoBack">&times;</span>
        <div class="text-center mb-4">
          <h3 class="display-4 text-dark font-bold">Εγγραφείτε</h3>
        </div>
        
        <form @submit.prevent="onFormSubmit">
          <div class="form-group">
            <label for="firstNameFormControl" class="form-label text-dark">Όνομα</label>
            <input v-model="user.fname" type="text" class="form-control rounded-3" id="firstNameFormControl" />
          </div>
          
          <div class="form-group">
            <label for="lastNameFormControl" class="form-label text-dark">Επώνυμο</label>
            <input v-model="user.lname" type="text" class="form-control rounded-3" id="lastNameFormControl" />
          </div>
          
          <div class="form-group">
            <label for="emailFormControl" class="form-label text-dark">Email</label>
            <input v-model="user.email" type="text" class="form-control rounded-3" id="emailFormControl" />
          </div>
          
          <div class="form-group">
            <label for="usernameFormControl" class="form-label text-dark">Όνομα Χρήστη</label>
            <input v-model="user.username" type="text" class="form-control rounded-3" id="usernameFormControl" />
          </div>
          
          <div class="form-group">
            <label for="passwordFormControl" class="form-label text-dark">Κωδικός Πρόσβασης</label>
            <input v-model="user.password" type="password" class="form-control rounded-3" id="passwordFormControl" />
          </div>
          
          <div class="form-group">
            <label for="userRoleFormControl" class="form-label text-dark">Ρόλος</label>
            <select v-model="user.role" class="form-control rounded-3" id="userRoleFormControl" @change="togglerOwnerFields">
              <option value="ROLE_TENANT">Tenant</option>
              <option value="ROLE_OWNER">Owner</option>
            </select>
          </div>
          
          <div class="form-group">
              <label for="phoneFormControl" class="form-label text-dark">Κινητό</label>
              <input v-model="user.phoneNumber" type="text" class="form-control rounded-3" id="phoneFormControl" />
          </div>
          
          <div class="text-center">
            <button type="submit" class="btn btn-primary btn-lg rounded-3 font-bold">Καλώς Ορίσατε!</button>
          </div>
        </form>
        
        <div class="h6 text-danger text-center mt-3">{{ msg }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const user = ref({
  username: '',
  email: '',
  password: '',
  role: '',
  fname: '',
  lname: '',
  phoneNumber: ''
});

const msg = ref('');
const showModal = ref(true);
const router = useRouter();

const onFormSubmit = () => {
  const endpoint = `http://localhost:8080/api/auth/signup`; // Use backendURL variable here
  const userData = {
    username: user.value.username,
    email: user.value.email,
    password: user.value.password,
    role: [user.value.role],
    fname: user.value.fname,
    lname: user.value.lname,
    phoneNumber: user.value.role === 'ROLE_OWNER' ? user.value.phoneNumber : null
  };


  fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })
      .then((response) => {
        if (response.ok) {
          msg.value = 'User registered successfully! Redirecting to login...';

          setTimeout(() => {
            router.push("/login");
          }, 2000);
        } else {
          msg.value = 'Failed to register user. Please try again.';
          console.log(JSON.stringify(userData));
        }
      })
      .catch((error) => {
        console.error('Error during registration:', error);
        msg.value = 'An error occurred during registration. Please try again.';
      });
};

const togglerOwnerFields = () => {
  //Reset owner-specific fields when switching roles
  if (user.value.role !== 'ROLE_OWNER') {
    user.value.phoneNumber = '';
  }
};

const openModal = (message) => {
  modalMessage.value = message;
  showModal.value = true;
};

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

/* Modal Container */
.modal-container {
  background-color: #9E6871;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  max-width: 500px;
  width: 100%;
  text-align: center;
}

/* ScrollBar */
.modal-content {
  max-height: 80vh;
  overflow-y: auto;
  padding: 1rem;
}

/* Heading and Text */
h3 {
  font-size: 2rem;
  color: #333;
  font-weight: 700;
  margin-bottom: 2rem;
}

/* Form styling */
.form-group {
  margin-bottom: 1.5rem;
}

label {
  font-size: 1rem;
  color: #333;
  margin-bottom: 0.5rem;
  display: block;
}

/* Inputs */
input.form-control,
select.form-control {
  padding: 12px;
  border-radius: 5px;
  border: 1px solid #ccc;
  width: 80%;
  font-size: 1rem;
  margin-bottom: 1rem;
  transition: border-color 0.3s ease;
}

input.form-control:focus,
select.form-control:focus {
  border-color: #a86f72;
  outline: none;
}

button.btn {
  background-color: #80535A;
  color: white;
  padding: 15px 25px;
  font-size: 1.2rem;
  border-radius: 5px;
  cursor: pointer;
  align-content: center;
  width: 40%;
  transition: background-color 0.3s ease;
  border: none;
}

button.btnX {
  background-color: #80535A;
  color: white;
  padding: 5px 9px;
  font-size: 1.2rem;
  border-radius: 5px;
  cursor: pointer;
  align-content: right;
  width: 15%;
  transition: background-color 0.3s ease;
  border: none;
}

button.btn:hover {
  background-color: #926c7a; /* Darker blue on hover */
}

/* Error Message */
.text-danger {
  color: #38120e; /* Red color for error messages */
  font-size: 1rem;
  margin-top: 1rem;
}

/* Responsive Design */
@media (max-width: 768px) {
  .modal-container {
    padding: 1.5rem;
    max-width: 90%;
  }

  h3 {
    font-size: 1.5rem; /* Slightly smaller font on mobile */
  }

  .form-group {
    margin-bottom: 1rem;
  }
}

</style>