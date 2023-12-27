<template>
  <v-container class="py-8 px-6" fluid>
    <v-row>
      <v-app-bar app color="#071551" dark class="conBar">
    <v-btn @click="sortContacts" dark icon>
      <v-icon>mdi-sort</v-icon>
    </v-btn>
    <v-btn  @click="hidden = !hidden" dark icon>
      <v-icon>mdi-account-search</v-icon>
    </v-btn>
    <v-col md="3" v-if="!hidden">
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
      icon
      dark
      @click="searchContact"
    >
      <v-icon>mdi-magnify</v-icon>
    </v-btn>
    <!-- cancel all sort and search -->
    <v-btn @click="getContacts" dark icon>
      <v-icon>mdi-close</v-icon>
    </v-btn>
    <AddContact @addContact="addContact($event)" />
  </v-app-bar>
    </v-row>
    <v-row>
    <v-row>
      <v-col v-for="(contact, i) in contacts" :key="i" cols="12" sm="6" md="4" lg="4" class="contact-galery">
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
                @click="removeContact(contact)"
              >
                <v-icon>mdi-account-remove-outline</v-icon>
              </v-btn>
            </div>
          </div>
          <!-- <v-divider></v-divider> -->
          <div class="email-list">
            <div v-for="(email, j) in contact.emails" :key="j" class="email">
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
    <v-snackbar v-model="snackbar">
      {{ text }}
      <template v-slot:action="{ attrs }">
        <v-btn color="blue" text v-bind="attrs" @click="snackbar = false">Close</v-btn>
      </template>
    </v-snackbar>
    <div class="text-center mb-15">
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
    // axios
    //   .get('http://localhost:8080/api/GetContactPage', {
    //     params: {
    //       PageNumber: this.page,
    //     },
    //   })
    //   .then((Response1) => {
    //     const Data1 = Response1.data;
    //     this.contacts = Data1;
    //   });
    this.getContacts();
  },
  methods: {
    removeEmail(contactIndex, emailIndex) {
      var newContact = this.contacts[contactIndex];
      newContact.emails.splice(emailIndex, 1);
      if (newContact.emails.length == 0) {
        this.removeContact(newContact);
        this.text = 'Contact has no emails, it will be deleted';
        this.snackbar = true;
      } else {
        this.editContact(newContact);
        this.text = 'Email Successfully Deleted';
        this.snackbar = true;
      }      
    },
    getContacts() {
      this.hidden = true;
      // send token in headers
      axios
        .get('http://localhost:8080/contacts', {
          headers: {
            authorization: `${localStorage.getItem('token')}`,
          },
        })
        .then((Response) => {
          const Data = Response.data;
          this.contacts = Data;
        });
    },

    removeContact(contact) {
      axios.delete('http://localhost:8080/contacts/' + contact.id).then(() => {
        this.text = 'Contact Successfully Deleted';
        this.snackbar = true;
        this.getContacts();
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
        .post('http://localhost:8080/contacts', contact, {
          headers: {
            authorization: `${localStorage.getItem('token')}`,
          },
        })
        .then((Response) => {
          const Data = Response.data;
          this.text = Data;
          this.snackbar = true;
          this.getContacts();
        });
    },

    editContact(contact) {
      console.log(contact);
      axios.put('http://localhost:8080/contacts/' + contact.id, contact).then((Response) => {
        const Data = Response.data;
        console.log(Data);
        this.text = Data;
        this.snackbar = true;
        this.getContacts();
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
      axios.get('http://localhost:8080/contacts/search/' + this.search, {
        headers: {
          authorization: `${localStorage.getItem('token')}`,
        },
      }).then((Response) => {
        const Data = Response.data;
        this.contacts = Data;
      });
    },

    sortContacts() {
      axios.get('http://localhost:8080/contacts/sort', {
        headers: {
          authorization: `${localStorage.getItem('token')}`,
        },
      }).then((Response) => {
        const Data = Response.data;
        this.contacts = Data;
      });
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
.contact-galery {
  margin-top: 80px;
}
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
  transform: scale(1.05);
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

.conBar {
  padding: 10px;
  height: 80px !important;
  border-radius: 0 0 30px 30px !important;
}
</style>
