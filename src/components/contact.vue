<template>
    <v-container
        class="py-8 px-6"
        fluid
      >
        
        <v-img
          src="..\assets\maintittle6.jpg" 
          height="100" 
        >
            <AddContact @addContact = "addContact($event)"/>
        </v-img>
        <v-row>
          <v-row  align="start">
            <v-btn
                    icon 
                    x-large   
                    @click="hidden = !hidden"
                    >
                          <v-icon>mdi-account-search</v-icon>
                </v-btn>
                <v-col md="3">
                <v-fab-transition>
              <v-text-field
                v-show="!hidden"
                clearable
                filled
                outlined
                v-model="search"
                label="Search for"
              >
              </v-text-field>              
            </v-fab-transition> 
                </v-col>
            <v-fab-transition>
            <v-btn
                v-show="!hidden"
                fab
                small
                dark
                color="#2d3142"
                @click="searchContact"
                    >
                      <v-icon>mdi-checkbox-marked-circle</v-icon>
            </v-btn>
            </v-fab-transition> 
          </v-row>

        
          <v-col
            v-for="(contact,i) in contacts"
            :key=i
            cols="12"
          >
            <v-card>
              
              <v-subheader style="padding : 30px">
                <v-avatar><img src="..\assets\user.png" alt="user_photo" /></v-avatar>
                {{ contact.name }}
                
                <v-spacer></v-spacer>
                  <EditContact :Oldcontact="contact" :Oldname="contact.name" @editContact = "editContact($event)"/>

                  <AddEmailtoContact :contactobject="contact" @addEmailtoContact = "editContact($event)"/>
                  <v-btn
                      icon
                      color="red"
                      :key="i"
                      @click="removeContact(contact.name)"
                  >
                      <v-icon>mdi-account-remove-outline</v-icon>
                  </v-btn>

              </v-subheader>
              <v-divider></v-divider>
              <v-list two-line>
                
                  <v-list-item
                    v-for="(email,j) in contact.emailAddresses"
                    :key=j
                    
                  >
                    <v-list-item-avatar  tile>
                      <v-img src="..\assets\mail.png"></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                      <v-list-item-title>Email no.{{ j+1 }}</v-list-item-title>

                      <v-list-item-subtitle>
                        {{email}}
                      </v-list-item-subtitle>
                    </v-list-item-content>
                    
                    <EditEmail :i= "i" :j= "j" @editEmail = "editEmail($event)"/>


                    <v-btn
                      class="mx-6"
                      icon
                      @click="removeEmail(i,j)"
                    >
                      <v-icon>mdi-close</v-icon>
                    </v-btn>

                    <v-snackbar
                      v-model="snackbar"
                    >
                      {{ text }}

                      <template v-slot:action="{ attrs }">
                        <v-btn
                          color="blue"
                          text
                          v-bind="attrs"
                          @click="snackbar = false"
                        >
                          Close
                        </v-btn>
                      </template>
                    </v-snackbar>

                  </v-list-item>
                
              </v-list>
            </v-card>
          </v-col>
        </v-row>
        <div class="text-center">
          <v-pagination
            v-model="page"
            :length=numberOfPages
            :disabled="disable"
            @input="changeThePage"
          ></v-pagination>
        </div>
      </v-container>
</template>

<script>
import AddContact from './addContact.vue'
import EditEmail from './editEmail.vue'
import AddEmailtoContact from './addEmailtoContact.vue'
import EditContact from './editContact.vue'



import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'

export default {
    props :['contacts','numberOfPages'],
    data: () => ({
        newContact : {},
        newEmail : '' ,
        text : '',
        snackbar : false ,
        page : 1 ,
        search: '',
        hidden: true,
        disable: ''
    }),
    components : {
        AddContact,
        EditEmail,
        AddEmailtoContact,
        EditContact
    },
    methods : {
        
        removeEmail(i,j){
            var oldname = this.contacts[i].name ,
            //take copy of it
            newContact = JSON.parse(JSON.stringify(this.contacts[i]));
            newContact.emailAddresses.splice(j,1);

            if( newContact.emailAddresses.length == 0){
                this.removeContact(newContact.name);
            }
            else{
              axios.post('http://localhost:8080/api/editContact?oldName=' + newContact.name,{
                name : newContact.name,
                emailAddresses : newContact.emailAddresses
              }).then(Response=>{
                  const Data = Response.data;
                  this.text = Data ;
                  this.snackbar = true ;
                axios.get('http://localhost:8080/api/GetContactPage',{
                params: {
                  //this must be varable global
                    PageNumber : this.page
                }
                }).then(Response1=>{
                const Data1 = Response1.data;
                this.contacts = Data1 ;
              });  
          });

            }
        },

        removeContact(name) {
          axios.get('http://localhost:8080/api/removeContact',{
            params: {
                Name : name
            }
          }).then(Response=>{
            const Data = Response.data;
            this.text = Data ;
            this.snackbar = true ;
            if (this.text == "Contact Successfully Deleted"){
                  this.getNumberOfPages();
                  axios.get('http://localhost:8080/api/GetContactPage',{
                    params: {
                        PageNumber : this.page
                    }
                  }).then(Response1=>{
                    const Data1 = Response1.data;
                    this.contacts = Data1 ;
                  });
              }
          });
        },

        editEmail(n){
          var i = n[0] , j = n[1] , newEmail = n[2] ;
          var oldname = this.contacts[i].name ,
          newContact = JSON.parse(JSON.stringify(this.contacts[i]));
          newContact.emailAddresses[j] = newEmail ;
          
          axios.post('http://localhost:8080/api/editContact?oldName=' + newContact.name,{
            name : newContact.name,
            emailAddresses : newContact.emailAddresses
          }).then(Response=>{
            const Data = Response.data;
            this.text = Data ;
            this.snackbar = true ;
            axios.get('http://localhost:8080/api/GetContactPage',{
                params: {
                  //this must be varable global
                    PageNumber : this.page
                }
            }).then(Response1=>{
                const Data1 = Response1.data;
                this.contacts = Data1 ;
            });  
          });
        },


        addContact(contact){
            axios.post('http://localhost:8080/api/addContact',{
                name : contact.name,
                emailAddresses : contact.emailAddresses
            }).then(Response=>{
              const Data = Response.data;
                this.text = Data ;
                this.snackbar = true ;
                if (this.text == "Successfully added contact"){
                  axios.get('http://localhost:8080/api/GetContactPage',{
                    params: {
                        PageNumber : this.page
                    }
                  }).then(Response1=>{
                    const Data1 = Response1.data;
                    this.contacts = Data1 ;

                  });
                  this.getNumberOfPages();
              }
            });
        },

        editContact(n){
          //n[0] oldname of contact
          //n[1] new contact with new email
          console.log(n);
          axios.post('http://localhost:8080/api/editContact?oldName=' + n[0],{
            name : n[1].name,
            emailAddresses : n[1].emailAddresses
          }).then(Response=>{
            const Data = Response.data;
            this.text = Data ;
            this.snackbar = true ;
            axios.get('http://localhost:8080/api/GetContactPage',{
                params: {
                  //this must be varable global
                    PageNumber : this.page
                }
            }).then(Response1=>{
                const Data1 = Response1.data;
                this.contacts = Data1 ;
            });  
          });
        },

        changeThePage(){
          axios.get('http://localhost:8080/api/GetContactPage',{
            params: {
              //this must be varable global
                PageNumber : this.page
            }
            }).then(Response=>{
              const Data = Response.data;
              this.contacts = Data ;
          });  
        },

        getNumberOfPages(){
          axios.get('http://localhost:8080/api/ContactsNumberOfPages',{
              }).then(Response=>{
                  const Data = Response.data;
                  this.numberOfPages = Data ;
          });
        },
        searchContact() {
          axios.post('http://localhost:8080/api/SearchContact?Name=' + this.search,{
          }).then(Response=>{
                const Data = Response.data;
                this.contacts = [Data] ;
          });
        }
    }
}
</script>

<style>

</style>