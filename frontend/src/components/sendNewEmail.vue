<template>
  <v-container class="py-8 px-6 sendEmail mt-10" fluid>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card color="#BFD7ED">
          <v-card-text>
            <v-row>
              <v-col md="6">
                <v-row>
                <template>
                  <!-- style the chip -->
                  <v-select
                    v-model="receiversNames"
                    :items="contacts"
                    chips
                    label="To"
                    multiple
                    prepend-icon="mdi-account"
                    solo
                    filled
                    dense
                    item-text="name"
                  ></v-select>
                </template></v-row>
                <!-- <v-row v-for="(receiver, r) in receiverEmailAddress" :key="r">
                  <v-text-field
                    v-model="receiverEmailAddress[r]"
                    label="To"
                    required
                    prepend-icon="mdi-account"
                    solo
                    dense
                    filled
                  ></v-text-field>
                  <v-btn v-if="r === 0" icon @click="addReceiver" color="#071551">
                      <v-icon>mdi-account-plus</v-icon>
                  </v-btn>
                  <v-btn v-if="r === 1" icon @click="removeReceiver" color="#071551">
                      <v-icon>mdi-account-minus</v-icon>
                  </v-btn>
                  <v-col v-if="r > 1" md="1"></v-col>
                </v-row> -->
              </v-col>
              <v-col md="8">
                <v-row>
                  <v-text-field
                    v-model="subject"
                    label="Subject"
                    required
                    prepend-icon="mdi-text-short"
                    solo
                    dense
                    filled
                  ></v-text-field>
                </v-row>
              </v-col>
              <v-col md="10">
                <v-row>
                  <v-textarea
                    filled
                    name="input-7-4"
                    label="Message"
                    v-model="body"
                    prepend-icon="mdi-message-text"
                    solo
                    dense
                  ></v-textarea>
              </v-row>
            </v-col>
              <v-row>
                <v-col md="4">
                  <v-file-input
                    v-model="attachments"
                    placeholder="Upload your documents"
                    label="File input"
                    multiple
                    prepend-icon="mdi-paperclip"
                    solo
                    filled
                    dense
                  >
                  </v-file-input>
                </v-col>
                <v-col md="8">
                  <v-chip v-for="fileName in attachments" :key="fileName.name" class="me-2 mt-2" color="primary">
                    {{ fileName.name }}
                  </v-chip>
                </v-col>
              </v-row>  
            </v-row>
            <v-row>
              <v-col md="6">
                <span class="subheading">Mail Importance</span>
                <v-card-text>
                  <v-slider
                    v-model="importance"
                    append-icon="mdi-priority-high"
                    prepend-icon="mdi-priority-low"
                    :min="1"
                    :max="4"
                    step="1"
                    tick-size="4"
                    dense
                    hide-details
                    color="#071551"
                    thumb-label
                    @click:append="importance++"
                    @click:prepend="importance--"
                  ></v-slider>
                </v-card-text>
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
  props : ["senderEmailAddress"] ,
  data: () => ({
      snackbar : false ,
      text : "" ,
      receiverEmailAddress : [''] ,
      subject : '',
      body : '',
      attachments : [],
      file : null,
      importanceList: ['low','medium','important','very important'],
      importanceList2: ['low','medium','Important','veryImportant'],
      importance: 1,
      contacts: [],
      receivers: [],
      receiversNames: [],
  }),
  mounted () {
      this.getContacts();
  },
  methods : {
      reset : function(){
          this.receiverEmailAddress = [''] ;
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
      send(isDraft){
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


        console.log(this.receivers);
        axios.post('http://localhost:8080/emails', {
            // to: this.receiverEmailAddress,
            to: this.receivers,
            subject: this.subject,
            body: this.body,
            attachments: this.attachments,
            priority: this.importance,
            isDraft: isDraft
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
      removeAttachment(){
          this.attachments.pop();
      },
      getContacts() {
        axios.get('http://localhost:8080/contacts', {
          headers: {
            authorization: `${localStorage.getItem('token')}`
          }
        }).then((response => response.data))
          .then((data) => {
            this.contacts = data;
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

.col{
  padding: 0 15px !important;
}
</style>