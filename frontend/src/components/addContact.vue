<template>
    <v-row justify="center" class="justify-center">
      <v-dialog v-model="dialog" width="400px" color="#ebf1fc">
        <template v-slot:activator="{ on, attrs }">
          <v-spacer></v-spacer>
          <v-btn
            class="addnewcontact"
            right
            icon
            elevation="2"
            dark
            v-bind="attrs"
            v-on="on"
          >
            <v-icon>mdi-account-plus</v-icon>
          </v-btn>
        </template>
        <v-card color="#ebf1fc">
          <v-card-title>
            <v-img
                src="..\assets\user.png"
                alt="user_photo"
                class="avatar-image"
              ></v-img>
          </v-card-title>
  
          <v-card-text>
            <v-col cols="12">
              <v-text-field
                v-model="newUser.name"
                label="Name"
                outlined
                filled
                required
                dense
                hide-details
              ></v-text-field>
            </v-col>
  
            <v-col cols="12">
              <v-text-field
                v-model.number="numberOfEmails"
                label="Number of emails"
                min="1"
                type="number"
                required
                filled
                outlined
                dense
                hide-details
              ></v-text-field>
            </v-col>
  
            <v-col cols="12">
              <v-text-field
                v-for="i in Math.max(numberOfEmails,1)"
                :key="i"
                v-model="newUser.emails[i - 1]"
                type="email"
                label="Email"
                required
                filled
                outlined
                dense
                hide-details
              ></v-text-field>
            </v-col>
            <v-col class="text-center" cols="12">
            <v-btn dark color="#071551" @click="addContact" class="mt-2" center>Add Contact</v-btn>
            </v-col>
          </v-card-text>
        </v-card>
      </v-dialog>
    </v-row>
  </template>
  
  <script>
  export default {
    data() {
      return {
        dialog: false,
        newUser: { name: '', emails: [] },
        numberOfEmails: 1,
      };
    },
    methods: {
      addContact() {
        this.$emit('addContact', this.newUser);
        this.dialog = false;
        // Optionally, you can reset the form here if needed
        this.newUser = { name: '', emails: [] };
        this.numberOfEmails = 1; // Reset the number of emails for the next use
      },
    },
  };
  </script>
  
  <style scoped>
  .addnewcontact {
    margin: 25px;
    /* border-radius: 15px; */
  }
  .avatar-image {
  max-width: 60px;
  border-radius: 50%;
}
  </style>
  