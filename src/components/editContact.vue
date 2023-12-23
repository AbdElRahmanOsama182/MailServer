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
            background-color="green"
            v-bind="attrs" 
            v-on="on"
        >
            Edit Contact
        </v-btn>

      </template>

      <v-card>
        <v-list>
          <v-list-item>
            <v-col>
                <h2>Name</h2>
                <v-text-field
                    v-model="newContact.name"
                    required
                ></v-text-field>
            </v-col>
          </v-list-item>

            <v-divider></v-divider>

          <v-list-item v-for="(email,i) in newContact.emailAddresses"
          :key = i>
            <v-col>
                <h2>E-mail no. {{i+1}}</h2>
                <v-text-field
                    v-model="newContact.emailAddresses[i]"
                ></v-text-field>
            </v-col>
          </v-list-item>
          

        </v-list>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="menu = false">Cancel</v-btn>
          <v-btn color="primary" @click="edit()">Edit</v-btn>
        </v-card-actions>

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