<template>
  <div>
    <v-menu
      v-model="menu"
      :close-on-content-click="false"
      :nudge-width="300"
      persistent
      
    >
      <template v-slot:activator="{ on, attrs }">
        <!-- <v-btn icon color="primary" @click="editContactDialog(contact)">
                <v-icon>mdi-pencil</v-icon>
              </v-btn> -->
        <v-btn
            icon color="primary"
            class="mx-4"
            background-color="green"
            v-bind="attrs" 
            v-on="on"
        >
          <v-icon>mdi-pencil</v-icon>
        </v-btn>

      </template>

      <v-card>
        <!-- remove padding between list items -->
        <v-list color="#BFD7ED" dense>
          <v-list-item>
            <v-col>
                <v-text-field
                    v-model="newContact.name"
                    label="Name"
                    outlined
                    filled
                    dense
                    required
                    hide-details
                ></v-text-field>
            </v-col>
          </v-list-item>
          <v-list-item v-for="(email,i) in newContact.emailAddresses"
          :key = i >
            <v-col>
                <v-text-field
                    v-model="newContact.emailAddresses[i]"
                    :label="'Email ' + (i+1)"
                    outlined
                    filled
                    dense
                    hide-details
                    required
                ></v-text-field>
            </v-col>
          </v-list-item>
          <v-list-item>
            <v-spacer></v-spacer>
          <v-btn text @click="menu = false">Cancel</v-btn>
          <v-btn color="#071551" @click="edit()" dark>Edit</v-btn>
          </v-list-item>
        </v-list>
      </v-card>
    </v-menu>
  </div>
</template>

<script>
  export default {
    props :['Oldcontact','Oldname'],
    data: function (){
        return {
            menu: false,
            newContact : "to" ,
        }
    },
    mounted () {
        this.newContact = JSON.parse(JSON.stringify(this.Oldcontact));
    },
    methods : {
      edit(){
        this.menu = false ;
        this.$emit('editContact',[this.Oldcontact.name,this.newContact]);
      }
    }
  }
</script>
<style scoped>

</style>