<template>
  <div class="text-center mb-4">
    <h1 class="fs-3">Προσθήκη Ακινήτου</h1>
  </div>
  <div class="overlay white-text box">
    <div class="form-container">
      <div class="modal-container">
        <div class="content">

          <form @submit.prevent="onFormSubmit">
            <div class="mb-2">
              <label for="cityFormControl" class="form-label mb-1">Πόλη</label>
              <div class="col-sm-8">
                <input v-model="property.city" type="text" class="form-control" id="cityFormControl" pattern="[A-Za-z]+" title="Please enter only letters" required />
              </div>
            </div>

            <div class="mb-2">
              <label for="streetFormControl" class="form-label mb-1">Οδός</label>
              <div class="col-sm-8">
                <input v-model="property.street" type="text" class="form-control" id="streetFormControl" pattern="[A-Za-z]+" title="Please enter only letters" required />
              </div>
            </div>

            <div class="mb-2">
              <label for="streetNumFormControl" class="form-label mb-1">Νούμερο</label>
              <div class="col-sm-4">
                <input v-model="property.streetNum" type="number" class="form-control" id="streetNumFormControl"  min="0" title="Please enter only numbers" required />
              </div>
            </div>

            <div class="mb-2">
              <label for="priceFormControl" class="form-label mb-1">Τιμή (σε €)</label>
              <div class="col-sm-4">
                <input v-model="property.price" type="number" class="form-control" id="priceFormControl"  min="0" title="Please enter only numbers" required />
              </div>
            </div>
            
            <div class="mb-2">
              <label for="sizeFormControl" class="form-label mb-1">Μέγεθος (σε τ.μ)</label>
              <div class="col-sm-4">
                <input v-model="property.size" type="number" class="form-control" id="sizeFormControl"  min="0" title="Please enter only numbers" required />
              </div>
            </div>
            
            <div class="mb-2">
              <label for="roomNumFormControl" class="form-label mb-1">Σύνολο Δωματίων</label>
              <div class="col-sm-4">
                <input v-model="property.roomNum" type="number" class="form-control" id="roomNumFormControl"  min="0" title="Please enter only numbers" required />
              </div>
            </div>
            
            <div class="mb-2">
              <label for="bedNumFormControl" class="form-label mb-1">Εκ των οποίων κρεβατοκάμαρες</label>
              <div class="col-sm-4">
                <input v-model="property.bedNum" type="number" class="form-control" id="bedNumFormControl"  min="0" title="Please enter only numbers" required />
              </div>

            </div>
            <div class="mb-2">
              <label for="bathNumFormControl" class="form-label mb-1">Αριθμός WC</label>
              <div class="col-sm-4">
                <input v-model="property.bathNum" type="number" class="form-control" id="bathNumFormControl"  min="0" title="Please enter only numbers" required />
              </div>
            </div>
            
            <div class="mb-2">
              <label for="builtYearFormControl" class="form-label mb-1">Έτος Κατασκευής</label>
              <div class="col-sm-4">
                <input v-model="property.builtYear" type="number" class="form-control" id="builtYearFormControl"  min="1950" max="2025" title="Please enter only numbers" required />
              </div>
            </div>

            <div class="mb-2">
              <label for="renovationYearFormControl" class="form-label mb-1">Έτος Ανακαίνησης (Αν όχι, βάλτε το ίδιο έτος)</label>
              <div class="col-sm-4">
                <input v-model="property.renovationYear" type="number" class="form-control" id="renovationYearFormControl"  min="1950" max="2025" title="Please enter only numbers" required />
              </div>
            </div>

            <div class="mb-2">
              <label for="isFurnishedFormControl" class="form-label mb-1">Είναι Επιπλωμένο;</label>
              <div class="col-sm-4">
                <select v-model="property.isFurnished" class="form-select" id="isFurnishedFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>
            
            <div class="mb-2">
              <label for="hasParkingFormControl" class="form-label mb-1">Έχει χώρο για Πάρκινγκ;</label>
              <div class="col-sm-4">
                <select v-model="property.hasParking" class="form-select" id="hasParkingFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label for="hasBalconyFormControl" class="form-label mb-1">Έχει μπαλκόνια;</label>
              <div class="col-sm-4">
                <select v-model="property.hasBalcony" class="form-select" id="hasBalconyFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label for="hasGardenFormControl" class="form-label mb-1">Έχει κήπο;</label>
              <div class="col-sm-4">
                <select v-model="property.hasGarden" class="form-select" id="hasGardenFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label for="hasHeatingFormControl" class="form-label mb-1">Έχει κεντρική θέρμανση;</label>
              <div class="col-sm-4">
                <select v-model="property.hasHeating" class="form-select" id="hasHeatingFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label for="hasACFormControl" class="form-label mb-1">Ψύξη;</label>
              <div class="col-sm-4">
                <select v-model="property.hasAC" class="form-select" id="hasACFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>

            <div class="mb-2">
              <label for="petsAllowedFormControl" class="form-label mb-1">Επιτρέπονται τα κατοικίδια;</label>
              <div class="col-sm-4">
                <select v-model="property.petsAllowed" class="form-select" id="petsAllowedFormControl" required>
                  <option :value="true">Ναι</option>
                  <option :value="false">Όχι</option>
                </select>
              </div>
            </div>


            <button type="submit" class="btn btn-primary btn-lg rounded-3 font-bold">Βρες μου αγοραστή!</button>
          </form>

          <div v-if="showModal" class="modal">
            <div class="modal-content success">
              <span class="close" @click="closeModal">&times;</span>
              <p>{{ modalMessage }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useApplicationStore } from '@/stores/application.js';

const { loadUserData } = useApplicationStore();
const userData = loadUserData();

const property = ref({
  city : '',
  street: '',
  streetNum: '',
  price: '',
  size: '',
  roomNum: '',
  bathNum: '',
  bedNum: '',
  isFurnished: '',
  hasParking: '',
  hasBalcony: '',
  hasGarden: '',
  hasHeating: '',
  hasAC: '',
  petsAllowed: '',
  builtYear: '',
  renovationYear: ''
});
const showModal = ref(false);
const modalMessage = ref('');
const openModal = (message) => {
  modalMessage.value = message;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
};

const onFormSubmit = () => {
  fetch('http://localhost:8080/api/owner/newProperty', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${userData.token}`,
    },
    body: JSON.stringify(property.value),
  })
      .then((response) => {
        if (response.ok) {
          openModal('Η Αγγελία σας είναι έτοιμη!'); //if response is ok display saved
          clearForm();
        } else {
          openModal('Η Αγγελία σας δεν μπόρεσε να αποθηκευτεί.');//response not ok show failed message
        }
      })
      .catch((error) => {
        console.log = `Error: ${error}`;
      });
};

const clearForm = () => {
  property.value = {
    city : '',
    street: '',
    streetNum: '',
    price: '',
    size: '',
    roomNum: '',
    bathNum: '',
    bedNum: '',
    isFurnished: '',
    hasParking: '',
    hasBalcony: '',
    hasGarden: '',
    hasHeating: '',
    hasAC: '',
    petsAllowed: '',
    builtYear: '',
    renovationYear: ''
  };

};
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  display: block;
  text-align: center;
  background-color: #f4f4f4;
  font-family: 'Roboto', sans-serif;
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
  background-color: #80535A;
  color: #fff;
  padding: 30px;
  border-radius: 8px;
  border: 1px solid #444;
  width: 90%;
  max-width: 600px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  z-index: 1002;
  margin: auto;
}

.modal-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.close {
  color: #fff;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  transition: color 0.3s;
}

.close:hover,
.close:focus {
  color: #3498db;
}

.form-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  text-align: left;
  background-color: #80535A;
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  margin-top: 30px;
}

.form-control {
  width: 95%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
  margin-bottom: 1rem;
  transition: border-color 0.3s;
}

.form-control:focus {
  border-color: #3498db;
  outline: none;
}

.btn {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  border-radius: 8px;
  background: #a86f72;
  color: #ffffff;
  transition: background 0.3s ease, box-shadow 0.3s;
  border: none;
  outline: none;
  margin-top: 20px;
}

.btn:hover {
  background: #df585e;
  box-shadow: 0 0 12px #df585e;
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 15px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  z-index: 1000;
}

.success-popup {
  color: green;
  border: 2px solid green;
}

.failure-popup {
  color: red;
  border: 2px solid red;
}

.icon {
  margin-right: 8px;
}

.white-text {
  color: #fff;
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}

@media (max-width: 768px) {
  .form-container {
    width: 100%;
    padding: 1.5rem;
  }

  .btn {
    padding: 12px;
    font-size: 14px;
  }

  .form-control {
    padding: 10px;
    font-size: 14px;
  }
}
</style>