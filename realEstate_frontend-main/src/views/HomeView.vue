<template>
  <div class="zoomInUp">
    <h1>Ηλύσιον</h1>
    <div class="image-container">
      <img src="/src/assets/home_page_hero.jpg">
      <img src="/src/assets/home_page_2.jpg">
    </div>

    <p></p>
    <h3>Σπίτια. Δεν έχουμε και τίποτα άλλο...</h3>
  </div>
</template>

<script>
console.log(localStorage.getItem('userData'));
</script>

<style>
.zoomInUp {
  animation: zoomInUp 1.5s ease forwards;
}

h1 {
  font-size: 4rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}

h3 {
  font-size: 2rem;
  text-align: center;
  color: #373b55;
  text-shadow: 2px 2px 6px rgba(55, 59, 85, 0.5), -2px -2px 6px rgba(255, 255, 255, 0.2);
  margin: 20px 0;
}

.dark-text {
  text-align: center;
  color: #333;
  display: none;
}

.image-container img {
  position: relative;
  width: 100%;
  height: 100%;
  opacity: 0.7;
  margin-top: 0;
  object-fit: cover;
  object-position: center;
}

.image-container {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 400px;
  overflow: hidden;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

@keyframes zoomInUp {
  from {
    opacity: 0;
    transform: translateY(-150px);
  }
  to {
    opacity: 1;
    transform: translateY(-200px);
  }
}
</style>