<template>
  <v-container class="py-8 px-6" fluid>
    <v-row>
      <v-row>
        <v-btn  x-large @click="hidden = !hidden" color="#071551" fab dark>
          <v-icon>mdi-account-search</v-icon>
        </v-btn>
        <v-col md="3">
            <v-text-field
              v-show="!hidden"
              clearable
              filled
              outlined
              hide-details
              v-model="search"
              label="Search for a contact"
            ></v-text-field>
        </v-col>
        <v-btn
          v-show="!hidden"
          fab
          dark
          color='#071551'
          @click="searchContact">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <AddContact @addContact="addContact($event)" />
      </v-row>
    </v-row>
    <v-row>
    <v-row>
      <v-col v-for="(contact, i) in contacts" :key="i" cols="12" sm="6" md="4" lg="4">
        <v-card class="contact-card">
          <div class="contact-header">
            <div class="avatar">
              <v-img
                src="..\assets\user.png"
                alt="user_photo"
                class="avatar-image"
              ></v-img>
            </div>
            <div class="contact-details">
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
            </div>
          </div>
          <!-- <v-divider></v-divider> -->
          <div class="email-list">
            <div v-for="(email, j) in contact.emailAddresses" :key="j" class="email">
              <v-img src="..\assets\mail.png" class="email-icon"></v-img>
              <div class="email-details">
                <!-- <div class="email-title">Email no.{{ j + 1 }}</div> -->
                <div class="email-subtitle">{{ email }}</div>
              </div>
              <v-btn class="mx-6" icon @click="removeEmail(i, j)">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </div>
          </div>
        </v-card>
      </v-col>
    </v-row>
    </v-row>
    <div class="text-center">
      <v-pagination
        v-model="page"
        :length="numberOfPages"
        :disabled="disable"
        @input="changeThePage"
      ></v-pagination>
    </div>
  </v-container>
</template>


<script>
import AddContact from './addContact.vue';
import EditEmail from './editEmail.vue';
import AddEmailtoContact from './addEmailtoContact.vue';
import EditContact from './editContact.vue';

import Vue from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';

export default {
  props: ['contacts', 'numberOfPages'],
  data: () => ({
    newContact: {},
    newEmail: '',
    text: '',
    snackbar: false,
    page: 1,
    search: '',
    hidden: true,
    disable: '',
  }),
  components: {
    AddContact,
    EditEmail,
    AddEmailtoContact,
    EditContact,
  },
  mounted() {
    axios
      .get('http://localhost:8080/api/GetContactPage', {
        params: {
          PageNumber: this.page,
        },
      })
      .then((Response1) => {
        const Data1 = Response1.data;
        this.contacts = Data1;
      });
  },
  methods: {
    removeEmail(i, j) {
      var oldname = this.contacts[i].name,
        newContact = JSON.parse(JSON.stringify(this.contacts[i]));
      newContact.emailAddresses.splice(j, 1);

      if (newContact.emailAddresses.length == 0) {
        this.removeContact(newContact.name);
      } else {
        axios
          .post(
            'http://localhost:8080/api/editContact?oldName=' +
              newContact.name,
            {
              name: newContact.name,
              emailAddresses: newContact.emailAddresses,
            }
          )
          .then((Response) => {
            const Data = Response.data;
            this.text = Data;
            this.snackbar = true;
            axios
              .get('http://localhost:8080/api/GetContactPage', {
                params: {
                  PageNumber: this.page,
                },
              })
              .then((Response1) => {
                const Data1 = Response1.data;
                this.contacts = Data1;
              });
          });
      }
    },

    removeContact(name) {
      axios
        .get('http://localhost:8080/api/removeContact', {
          params: {
            Name: name,
          },
        })
        .then((Response) => {
          const Data = Response.data;
          this.text = Data;
          this.snackbar = true;
          if (this.text == 'Contact Successfully Deleted') {
            this.getNumberOfPages();
            axios
              .get('http://localhost:8080/api/GetContactPage', {
                params: {
                  PageNumber: this.page,
                },
              })
              .then((Response1) => {
                const Data1 = Response1.data;
                this.contacts = Data1;
              });
          }
        });
    },

    editEmail(n) {
      var i = n[0],
        j = n[1],
        newEmail = n[2];
      var oldname = this.contacts[i].name,
        newContact = JSON.parse(JSON.stringify(this.contacts[i]));
      newContact.emailAddresses[j] = newEmail;

      axios
        .post(
          'http://localhost:8080/api/editContact?oldName=' + newContact.name,
          {
            name: newContact.name,
            emailAddresses: newContact.emailAddresses,
          }
        )
        .then((Response) => {
          const Data = Response.data;
          this.text = Data;
          this.snackbar = true;
          axios
            .get('http://localhost:8080/api/GetContactPage', {
              params: {
                PageNumber: this.page,
              },
            })
            .then((Response1) => {
              const Data1 = Response1.data;
              this.contacts = Data1;
            });
        });
    },

    addContact(contact) {
      axios
        .post('http://localhost:8080/api/addContact', {
          name: contact.name,
          emailAddresses: contact.emailAddresses,
        })
        .then((Response) => {
          const Data = Response.data;
          this.text = Data;
          this.snackbar = true;
          if (this.text == 'Successfully added contact') {
            axios
              .get('http://localhost:8080/api/GetContactPage', {
                params: {
                  PageNumber: this.page,
                },
              })
              .then((Response1) => {
                const Data1 = Response1.data;
                this.contacts = Data1;
              });
            this.getNumberOfPages();
          }
        });
    },

    editContact(n) {
      axios
        .post('http://localhost:8080/api/editContact?oldName=' + n[0], {
          name: n[1].name,
          emailAddresses: n[1].emailAddresses,
        })
        .then((Response) => {
          const Data = Response.data;
          this.text = Data;
          this.snackbar = true;
          axios
            .get('http://localhost:8080/api/GetContactPage', {
              params: {
                PageNumber: this.page,
              },
            })
            .then((Response1) => {
              const Data1 = Response1.data;
              this.contacts = Data1;
            });
        });
    },

    changeThePage() {
      axios
        .get('http://localhost:8080/api/GetContactPage', {
          params: {
            PageNumber: this.page,
          },
        })
        .then((Response) => {
          const Data = Response.data;
          this.contacts = Data;
        });
    },

    getNumberOfPages() {
      axios
        .get('http://localhost:8080/api/ContactsNumberOfPages')
        .then((Response) => {
          const Data = Response.data;
          this.numberOfPages = Data;
        });
    },

    searchContact() {
      axios
        .post('http://localhost:8080/api/SearchContact?Name=' + this.search, {})
        .then((Response) => {
          const Data = Response.data;
          // if data is empty clear the contacts array
          if (Data.length == 0) 
            this.contacts = [];
          else
            this.contacts = [Data];
        });
        this.hidden = true;
    },

    editContactDialog(n){
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
  },
};
</script>

<style scoped>
.contact-card {
  max-width: 400px;
  border-radius: 10px;
  background-color: #BFD7ED;
  margin: 0 auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.contact-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.avatar-image {
  max-width: 60px;
  border-radius: 50%;
}

.contact-header {
  display: flex;
  align-items: center;
  padding: 15px 15px 0 15px;
}

.avatar {
  margin-right: 15px;
}

.contact-details {
  flex-grow: 1;
  display: flex;
  align-items: center;
}

.spacer {
  flex-grow: 1;
}

.email-list {
  margin-top: 15px;
}

.email {
  display: flex;
  align-items: center;
  padding: 0 12px;
}

.email-icon {
  max-width: 40px;
  margin-right: 15px;
}

.email-details {
  flex-grow: 1;
}

.email-title {
  font-weight: bold;
}

.remove-button {
  background-color: #ff6666;
  color: #fff;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 14px;
}

.text-center {
  margin-top: 20px;
}

.contact-header {
  display: flex;
  align-items: center;
}

.avatar {
  margin-right: 10px;
}

.contact-details {
  flex-grow: 1;
  display: flex;
  align-items: center;
}

.spacer {
  flex-grow: 1;
}

.remove-button {
  background-color: #ff6666;
  color: #fff;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 12px;
}
</style>
