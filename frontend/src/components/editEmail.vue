<template>
  <div>
    <v-menu
      v-model="menu"
      :close-on-content-click="false"
      :nudge-width="300"
      
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          class="mx-0"
          fab
          icon
          small
          v-bind="attrs" 
          v-on="on"
        >
        <v-icon dark>
          mdi-pencil
        </v-icon>
    </v-btn>
      </template>

      <v-card>
        <v-list>
          <v-list-item>
            <v-col>
                <h2>E-mail</h2>
                <v-text-field
                    v-model="newEmail"
                    placeholder="Enter New Email Here"
                    required
                ></v-text-field>
            </v-col>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="menu = false">Cancel</v-btn>
          <v-btn color="primary" @click="change()">Save</v-btn>
        </v-card-actions>

      </v-card>
    </v-menu>
  </div>
</template>

<script>
  export default {
    props : ["i","j"],
    data: () => ({
      menu: false,
      newEmail : ''
    }),
    methods : {
      change(){
        this.$emit('editEmail',[this.i,this.j,this.newEmail]);
      }
    }
  }
</script>
<style scoped>

</style>