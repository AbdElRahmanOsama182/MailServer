<template>
  <v-container class="py-8 px-6 sendEmail mt-10" fluid>
    <v-row>
      <v-app-bar app color="#071551" dark class="conBar"> 
        <h2 style="color: white;font-weight: 400;">Crafting connections, one email at a time.</h2>  
      </v-app-bar>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card class="px-3 mt-6" color="#ebf1fc" style="border-radius: 15px;">
          <v-card-text>
            <v-row>
              <v-col md="6">
                <v-row>
                  <template>
                    <v-select v-model="receiversNames" :items="contacts" chips label="To" multiple 
                      prepend-icon="mdi-account" outlined dense item-text="name"></v-select>
                  </template>
              </v-row>
              </v-col>
              <v-col md="8">
                <v-row>
                  <v-text-field v-model="subject" label="Subject" required prepend-icon="mdi-text-short" outlined dense
                    ></v-text-field>
                </v-row>
              </v-col>
              <v-col md="10">
                <v-row>
                  <v-textarea name="input-7-4" label="Message" v-model="body" prepend-icon="mdi-message-text" outlined
                    dense></v-textarea>
                </v-row>
              </v-col>
              <v-row>
                <v-col md="4">
                  <v-file-input v-model="files" @change="onFileChange" placeholder="Upload files" label="File input" multiple
                    prepend-icon="mdi-paperclip" outlined dense>
                  </v-file-input>
                </v-col>
                <v-col md="8">
                  <v-chip v-for="fileName in attachments" :key="fileName.name" class="me-2 mt-2" color="primary">
                    {{ fileName.name }}
                    <v-icon small @click="removeAttachment(fileName)">mdi-close</v-icon>
                  </v-chip>
                </v-col>
              </v-row>
            </v-row>
            <v-row>
              <v-col md="6">
                <span class="subheading">Mail Importance</span>
                  <v-slider v-model="importance" append-icon="mdi-priority-high" prepend-icon="mdi-priority-low" :min="1"
                    :max="4" step="1" tick-size="4" dense hide-details color="#071551" thumb-label
                    @click:append="importance++" @click:prepend="importance--"></v-slider>
              </v-col>
            </v-row>

            <v-card-actions>
              <v-btn text @click="reset" color="red">
                <v-icon large>mdi-cancel</v-icon>
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn @click="send(false)" icon>
                <v-icon large>mdi-content-save</v-icon>
              </v-btn>
              <v-btn color="#071551" text @click="send(true)">
                <v-icon large>mdi-send</v-icon>
              </v-btn>
            </v-card-actions>

            <v-snackbar v-model="snackbar">
              {{ text }}
              <template v-slot:action="{ attrs }">
                <v-btn color="blue" text v-bind="attrs" @click="snackbar = false">Close</v-btn>
              </template>
            </v-snackbar>

          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>  

<script>

import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import { createVuetify } from 'vuetify'
export default {
  props: ["senderEmailAddress"],
  data: () => ({
    snackbar: false,
    text: "",
    receiverEmailAddress: [''],
    subject: '',
    body: '',
    attachments: [],
    files: [],
    file: null,
    importanceList: ['low', 'medium', 'important', 'very important'],
    importanceList2: ['low', 'medium', 'Important', 'veryImportant'],
    importance: 1,
    contacts: [],
    receivers: [],
    receiversNames: [],
  }),
  mounted() {
    this.getContacts();
  },
  methods: {
    onFileChange(e) {
      // add the selected files to the attachments list
      if (this.files.length > 0) {
        for (let i = 0; i < this.files.length; i++) {
          this.attachments.push(this.files[i]);
        }
      }
      this.files = [];
    },
    reset: function () {
      this.receiverEmailAddress = [''];
      this.subject = '';
      this.body = '';
      this.attachments = [];
      this.file = null;
    },
    // addToAttachmentList : function(newfile){
    //     this.attachments.push(1);
    //     this.file = newfile ;
    //     console.log(this.file);
    // },
    async send(isDraft) {
      console.log(`${localStorage.getItem('token')}`);
      // get contacts of receivers from contacts list without __ob__: Observer
      this.receivers = [];
      for (let i = 0; i < this.receiversNames.length; i++) {
        for (let j = 0; j < this.contacts.length; j++) {
          if (this.receiversNames[i] === this.contacts[j].name) {
            const receiver = {
              id: this.contacts[j].id,
              name: this.contacts[j].name,
              emails: this.contacts[j].emails,
              username: this.contacts[j].username

            }
            this.receivers.push(receiver);
          }
        }
      }
      console.log("this.attachments", this.attachments)
      // send multipart/form-data request of attachments
      const formData = new FormData();
      for (let i = 0; i < this.attachments.length; i++) {
        formData.append('files', this.attachments[i]);
      }
      console.log("formData", formData)
      if (this.attachments.length > 0) {

        await axios.post('http://localhost:8080/attachment', formData, {
          headers: {
            authorization: `${localStorage.getItem('token')}`,
            'Content-Type': 'multipart/form-data'
          }
        }).then((response => [response.data]))
          .then((data) => {
            console.log(data);
            this.attachments = data;
          })

      }
      console.log("this.attachments", this.attachments)


        console.log(this.receivers);
        axios.post('http://localhost:8080/emails', {
            // to: this.receiverEmailAddress,
            to: this.receivers,
            subject: this.subject,
            body: this.body,
            attachments: this.attachments,
            priority: this.importance,
            draft: !isDraft
          }, {
            headers: {
              authorization: `${localStorage.getItem('token')}`
            }
        }).then((response => response.data))
          .then((data) => {
            console.log(data);
            if (data === '') {
              this.snackbar = true;
              this.text = 'Email not sent';
            }
            else {
              this.snackbar = true;
              if (!isDraft) {
                this.text = 'Email saved as draft';
              }
              else
                this.text = 'Email sent';
                this.reset();
            }
          })
      },
      addReceiver() {
          this.receiverEmailAddress.push('');
      },
      removeReceiver(){
          this.receiverEmailAddress.pop();
      },
      addAttachment(){
          this.attachments.push('');
      },
      removeAttachment(file){
        this.attachments.splice(this.attachments.indexOf(file), 1);
      },
      getContacts() {
        axios.get('http://localhost:8080/contacts', {
          headers: {
            authorization: `${localStorage.getItem('token')}`
          }
        }).then((response => response.data))
          .then((data) => {
            this.contacts = data.contacts;
            console.log(this.contacts);
        })
    },

  },

}
</script>

<style scoped>
.sendEmail {
  margin-bottom: 80px;
  width: 70% !important;
}

.conBar {
  padding: 10px;
  height: 80px !important;
  border-radius: 0 0 30px 30px !important;
}
.col {
  padding: 0 15px !important;
}
</style>