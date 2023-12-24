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
          <v-btn color="primary" @click="add()">Add</v-btn>
        </v-card-actions>

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
        this.newContact = JSON.parse(JSON.stringify(this.contactobject));
    },
    methods : {
      add(){
        this.newContact.emailAddresses.push(this.newEmail);
        this.menu = false ;
        this.$emit('addEmailtoContact',[this.contactobject.name,this.newContact]);
      }
    }
  }
</script>
<style scoped>

</style>