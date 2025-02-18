
import { useRouter } from 'vue-router';
import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

function checkJWT(token) {
    if (token === null || token === undefined) {
        return false;
    }
    const base64Url = token.split('.')[1];
    if (!base64Url) return false;
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(atob(base64));
    const currentTime = Math.floor(Date.now() / 1000);
    return currentTime < payload.exp;
}

export const useApplicationStore = defineStore('application', () => {
    const userData = ref(null);

    const setUserData = (tempUserData) => {
        userData.value = tempUserData;
    };

    const persistUserData = () => {
        localStorage.setItem('userData', JSON.stringify(userData.value));
    };

    const loadUserData = () => {
        let tempUserData = localStorage.getItem('userData');
        tempUserData = JSON.parse(tempUserData);
        if (tempUserData === null || tempUserData === undefined) {
            console.log('userData is null or undefined');
            return;
        }
        userData.value = tempUserData;
        return userData.value;
    };

    const clearUserData = () => {
        localStorage.setItem('userData', JSON.stringify(null));
        userData.value = null;

    };
    const isAuthenticated = computed(() => {
        return checkJWT(userData.value?.token);
    });
    const router = useRouter();
    const loading = ref(false);

    const logout = () => {
        loading.value = true;
        clearUserData();
        loading.value = false;
        router.push({ name: 'login' });

        setTimeout(() => {
            location.reload();
        }, 500);
    };


    return {
        userData,
        setUserData,
        persistUserData,
        loadUserData,
        clearUserData,
        isAuthenticated,
        checkJWT,
        logout
    };
});
