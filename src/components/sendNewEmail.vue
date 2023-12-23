<template>
     <v-container
        class="py-8 px-10"
        fluid
        style="background-color: #D1D4C9"
    >
        <v-row>
          <v-col
            cols="12"
          >
            <v-card
            color="#1B262C"
            >
                <v-card-title 
                class="align-center"
                >
                    <h2>Send Email</h2>
                </v-card-title>

                <v-card-text>
                    <v-row>
                      <v-col md="10">
                        <v-row v-for="(reciever,r) in receiverEmailAddress" :key=r>
                            <v-col md="1">
                                <span class="s">Send to:</span> 
                            </v-col>
                            <v-text-field
                                background-color="#D1D4C9"
                                v-model="receiverEmailAddress[r]"
                                required
                                >
                            </v-text-field>
                            
                        </v-row>
                      </v-col>       
                        <v-col >
                            <v-btn
                                color="#BBE1FA"
                                icon
                                @click="addReceiver"
                            >
                                <v-icon>mdi-account-plus</v-icon>
                            </v-btn>
                            <v-btn v-if="receiverEmailAddress.length>1"
                                color="#BBE1FA"
                                icon
                                @click="removeReceiver"
                            >
                                <v-icon>mdi-account-minus</v-icon>
                                </v-btn>
                        </v-col>
                    </v-row>

                    <v-row>
                      <v-col md="10">
                        <v-row>
                            <v-col md="1">
                                <span>Subject:</span> 
                            </v-col>
                            <v-text-field
                                background-color="#D1D4C9"
                                v-model="subject"
                                required
                                >
                            </v-text-field>
                        </v-row>
                      </v-col>       
                    </v-row>

                    

                    <v-col>
                    <v-textarea
                        background-color="#D1D4C9"
                        variant="outlined"
                        label="Email text"
                        filled
                        v-model="body"
                        
                    ></v-textarea>
                    </v-col>

                    <v-row>
                    <v-col md="6">
                    <v-btn color="#BBE1FA">
                    <v-file-input
                        v-model="file"
                        @click="addToAttachmentList(file)"
                        truncate-length="100"
                    >Add attachment
                    </v-file-input>
                    </v-btn>
                    </v-col>
                 
                    <v-col>
                        <span class="Pr">Priority</span>
                        <v-card-text>
                            <v-slider
                                color="green"
                                v-model="importance"
                                :tick-labels="importanceList"
                                :max="3"
                                step="1"
                                ticks="always"
                                tick-size="3"
                            ></v-slider>
                        </v-card-text>
                    </v-col>
                    </v-row>


                    <!-- <v-col v-for="i in attachments"
                    :key="i">
                    <v-list>
                        <v-list-item>{{i}}</v-list-item>
                    </v-list>
                    </v-col> -->


                    <v-divider style="background-color: white">
                    </v-divider>

                    <v-card-actions>
                        <v-btn
                        @click="reset">
                            Reset Email
                        </v-btn>


                        <v-spacer></v-spacer>
                        <v-btn color="#0F4C75">
                            <span>Cancel</span>
                        </v-btn>
                    
                    <v-btn color="#0F4C75"
                        @click="send"
                    >
                        <span>Send</span>
                    </v-btn>

                    </v-card-actions>

                    <v-snackbar
                      v-model="snackbar"
                    >
                      {{ text }}

                      <template v-slot:action="{ attrs }">
                        <v-btn
                          color="green"
                          text
                          v-bind="attrs"
                          @click="snackbar = false"
                        >
                          Close
                        </v-btn>
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
        importanceList: ['Low','Medium','Important','Ergent'],
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
        addToAttachmentList : function(newfile){
            this.attachments.push(1);
            this.file = newfile ;
            console.log(this.file);
        },
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
     }

}
</script>

<style>
.align-center {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #BBE1FA;
  font-size: 30px;
}

span{
    font-size: 20px;
    color: #BBE1FA;
}


</style>