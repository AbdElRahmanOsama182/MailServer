<template>
  <v-app>
    <router-view/>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
  }),
};
</script>

<style>
  #app{
    background-color: #5b5b60;
  }
  .theme--light.v-tabs-items {
  background-color: #ff0e0e;
}
</style>
