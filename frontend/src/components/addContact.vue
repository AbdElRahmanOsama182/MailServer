<template>
    <v-row justify="center">
        <v-dialog
        v-model="dialog"
        max-width="600px"
        >
        <template v-slot:activator="{ on, attrs }">
            <v-spacer></v-spacer>
            <v-btn class="addnewcontact"
                right
                color="primary"
                elevation="2"
                x-large
                dark
                v-bind="attrs"
                v-on="on"
            >Add New Contact</v-btn>
        </template>
            <v-card>

                <v-card-title>
                    <span>{{ currentTitle }}</span>
                </v-card-title>

                <v-window v-model="step">
                
                <v-window-item :value="1">
                    <v-card-text>
                            <v-col cols="12">
                            <v-text-field
                                v-model=newUser.name
                                label="Name of the New Contact*"
                                required
                                ></v-text-field>
                            </v-col>

                            <v-col cols="12">
                                <v-text-field
                                    v-model.number=numberOfEmails
                                    type="number"
                                    label="Number of Emails of this contact*"
                                    min="1"
                                    required>
                                </v-text-field>
                            </v-col>
                    </v-card-text>
                </v-window-item>

                <v-window-item :value="2">
                    <v-card-text>
                        <v-col cols="12" v-for="i in numberOfEmails"
                            :key="i">
                                <v-text-field
                                v-model="newUser.emailAddresses[i-1]"
                                label="Email*"
                                required
                                ></v-text-field>
                        </v-col>
                    </v-card-text>
                </v-window-item>
                
                </v-window>

                <v-divider></v-divider>

                <v-card-actions>
                <v-btn
                    :disabled="step === 1"
                    text
                    @click="step--"
                >
                    Back
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                    color="primary"
                    @click="nextaction"
                >
                    {{tittleButton}}
                </v-btn>
                </v-card-actions>
            </v-card>
    </v-dialog>
    </v-row>
</template>

<script>
    import Contact from './contact.vue'

    import Vue from 'vue'
    import axios from 'axios'
    import VueAxios from 'vue-axios'

  export default {
    data: function(){
        return {
            step : 1,
            dialog: false,
            numberOfEmails : 1,
            newUser : {name : '' ,emailAddresses : []}
        }
    },
    computed: {
      currentTitle () {
        switch (this.step) {
          case 1: return 'Adding New Contact'
          default: return 'Adding New Contact Emails'
        }
      },
      tittleButton (){
          switch (this.step) {
          case 1: return 'Next'
          default: return 'Add New Contact'
        }
      }
    },
    methods:{
        nextaction : function(){
            this.step++;
            if(this.step === 3){
                this.dialog = false ;
                this.addContact();
            }
        },
        addContact(){
            this.$emit('addContact',this.newUser);
        }
    }
}
</script>

<style scoped>
.addnewcontact{
    margin: 25px;
}
</style>