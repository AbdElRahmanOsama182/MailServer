<template>
  <div>
    <v-menu
      v-model="menu"
      :close-on-content-click="false"
      :nudge-width="300"
      persistent
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            class="mx-4"
            icon
            color="primary"
            v-bind="attrs" 
            v-on="on"
        >
            <v-icon>mdi-email-plus-outline</v-icon>
        </v-btn>

      </template>

      <v-card color="#BFD7ED" elevation="0">
        <v-list color="#BFD7ED">
          <v-list-item>
            <v-col>
                <v-text-field
                    v-model="newEmail"
                    label="New Email"
                    outlined
                    hide-details
                    filled
                    required
                ></v-text-field>
            </v-col>
          </v-list-item>
          <v-list-item>
            <v-spacer></v-spacer>
          <v-btn text @click="menu = false">Cancel</v-btn>
          <v-btn dark color="#071551" @click="add()" >Add</v-btn>
          </v-list-item>
        </v-list>

      </v-card>
    </v-menu>
  </div>
</template>

<script>
  export default {
    props :['contactobject'],
    data: function (){
        return {
            menu: false,
            newEmail : '',
            newContact : null ,
        }
    },
    mounted () {
        // this.newContact = JSON.parse(JSON.stringify(this.contactobject));
        this.newContact = this.contactobject;
    },
    methods : {
      add(){
        this.newContact.emails.push(this.newEmail);
        this.menu = false ;
        this.$emit('addEmailtoContact',this.newContact);
      }
    }
  }
</script>
<style scoped>

</style>