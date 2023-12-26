<template>
  <v-container class="py-8 px-6 sendEmail mt-10" fluid>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <v-card color="#BFD7ED">
          <v-card-text>
            <v-row>
              <v-col md="6">
                <v-row v-for="(receiver, r) in receiverEmailAddress" :key="r">
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
                </v-row>
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
              <v-btn color="#071551" text @click="send">
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
  }),
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
      send(){
          var dt = new Date;
          var dformat = `${
              (dt.getMonth()+1).toString().padStart(2, '0')}/${
              dt.getDate().toString().padStart(2, '0')}/${
              dt.getFullYear().toString().padStart(4, '0')} ${
              dt.getHours().toString().padStart(2, '0')}:${
              dt.getMinutes().toString().padStart(2, '0')}:${
              dt.getSeconds().toString().padStart(2, '0')}`;
          console.log(this.importanceList2[this.importance])  ;
          axios.post('http://localhost:8080/api/ComposeMail?send=true',{
              id : 0,
              sender : this.senderEmailAddress ,
              recievers : this.receiverEmailAddress ,
              seen : true ,
              date : dformat ,
              importance : this.importanceList2[this.importance] ,
              subject : this.subject ,
              body : this.body,
              attachements : this.attachments ,

          }).then(Response=>{
              const Data = Response.data;
              this.text = Data ;
              this.snackbar = true ;
          });  
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
      }
   },

}
</script>

<style scoped>
.sendEmail {
  margin-bottom: 80px;
}

.col{
  padding: 0 15px !important;
}
</style>