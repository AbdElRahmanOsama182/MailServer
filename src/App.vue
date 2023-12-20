<template>
  <EmailApp/>
</template>

<script>
import EmailApp from './components/EmailApp.vue';

export default {
    name: "App",
    components: { EmailApp }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  
  height: 100vh;
}
</style>