<template>
  <v-container class="py-8 px-6" fluid>
    <v-row>
      <v-app-bar app color="#071551" dark class="MailsBar">
        <v-btn @click="hidden3 = !hidden3" icon>
          <v-icon>mdi-email-search-outline</v-icon>
        </v-btn>
        <v-col md="2" v-if="!hidden3">
          <v-combobox v-show="!hidden3" v-model="search" :items="items" label="Search by" dense hide-details></v-combobox>
        </v-col>
        <v-col md="2" v-if="!hidden3">
          <v-text-field
            v-show="!hidden3"
            clearable
            filled
            outlined
            hide-details
            v-model="searchQuery"
            label="Email Search"
          ></v-text-field>
        </v-col>
        <v-btn
          v-show="!hidden3"
          icon
          dark
          @click="applyOperations">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
        <v-btn @click="hidden = !hidden" icon>
          <v-icon>mdi-sort</v-icon>
        </v-btn>
        <v-col md="2" v-if="!hidden">
            <v-combobox v-show="!hidden" v-model="sort" :items="items" label="Sort by" dense hide-details></v-combobox>
        </v-col>
        <v-btn
          v-show="!hidden"
          icon
          dark
          @click="applyOperations">
          <v-icon>mdi-sort-ascending</v-icon>
        </v-btn>
          <v-btn @click="hidden2 = !hidden2" icon>
            <v-icon>mdi-filter</v-icon>
          </v-btn>
      
        <v-col md="2" v-if="!hidden2">
            <v-combobox v-show="!hidden2" v-model="filter" :items="items" label="Filter by" dense hide-details></v-combobox>
        </v-col>
      
        <v-col md="2" v-if="!hidden2">
            <v-text-field v-show="!hidden2 && filter !== 'priority'" clearable dense v-model="filtername" hide-details></v-text-field>              
            <v-slider dark span v-show="!hidden2 && filter === 'priority'" v-model="importance" thumb-label :min="1" :max="4" step="1" ticks="always" tick-size="4"></v-slider> 
        </v-col> 
        <v-btn
          v-show="!hidden2"
          icon
          dark
          @click="applyOperations">
          <v-icon>mdi-filter-check</v-icon>
        </v-btn>
        <v-btn icon dark @click="BulkDelete">
          <v-icon>{{ this.folder !== 'Trash' ? 'mdi-delete' : 'mdi-delete-restore' }}</v-icon>
        </v-btn>
        <v-btn icon dark @click="startBulkMove">
          <v-icon>mdi-cursor-move</v-icon>
        </v-btn>
        <v-btn icon dark @click="cancelFilters">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-app-bar>
    </v-row>
    <v-row>
      <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
        <template v-slot:activator="{ on, attrs }">
          <v-row class="message-gallery">
            <v-col v-for="(message, i) in messages" :key="i" cols="12" sm="6" md="4" lg="3">
              <v-card class="message-card" @click="openMessage(i)" v-bind="attrs" v-on="on">
                <v-img class="message-image" src="..\assets\mail.png"></v-img>
                <v-card-text class="message-info">
                  <div class="message-info-item"><strong style="color: #071551;">From:</strong> {{ message.fromUserId }}</div>
                  <div class="message-info-item"><strong style="color: #071551;">To:</strong> {{ message.to[0]?message.to[0].name:"" }}</div>
                  <div class="message-info-item"><strong style="color: #071551;">Subject:</strong> {{ message.subject }}</div>
                  <div class="message-info-item"><strong style="color: #071551;">Body:</strong> {{ message.body.substring(0, 20) }}</div>
                  <div class="message-info-item"><strong style="color: #071551;">Date:</strong> {{ message.sendDate.substring(0,10) }}</div>
                  <div class="message-info-item"><strong style="color: #071551;">Priority:</strong> {{ message.priority === 1 ? 'Low' : message.priority === 2 ? 'Medium' : message.priority === 3 ? 'Important' : 'Very Important' }}</div>
                </v-card-text>
                <v-btn class="message-action" icon @click.stop="openMessage(i)" @click="deleteOrrestore(message)">
                  <v-icon>{{ message.deleted === false ? 'mdi-delete' : 'mdi-delete-restore' }}</v-icon>
                </v-btn>
                <v-btn class="message-action mr-8" icon @click.stop="openMessage(i)" @click="startMove(i)">
                  <v-icon>mdi-cursor-move</v-icon>
                </v-btn>
                <v-checkbox class="message-action mt-8" v-model="selectedMessages[i]" hide-details @click.stop="openMessage(i)"></v-checkbox>
                <v-dialog v-model="moveDialog" max-width="400" transition="dialog-bottom-transition">
                  <v-card color="#ebf1fc">
                    <v-card-title>Choose Destination Folder</v-card-title>
                    <v-card-text>
                      <v-select 
                        v-model="selectedFolder"
                        :items="AllFolders"
                        chips
                        solo
                        hide-details
                        filled
                        dense
                        item-text="name"
                        label="Destination Folder"></v-select>
                    </v-card-text>
                    <v-card-actions>
                      <v-btn @click="moveDialog = false" color="#071551" dark>Cancel</v-btn>
                      <v-spacer></v-spacer>
                      <v-btn @click="move" color="#071551" dark>Move</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-card>
            </v-col>
          </v-row>
        </template>

        <ViewMail :mail="mail" @reply= "reply($event)" @click.native="dialog = false" 
         ></ViewMail>
      </v-dialog>
    </v-row>
    <v-row class="cont" v-if="numberOfPages">
      <div class="text-center mb-15">
        <v-pagination 
          v-model="page" 
          color="#071551"
          :length="numberOfPages" 
          :disabled="disable" 
          @input="changeThePage"></v-pagination>
      </div>
    </v-row>
  </v-container>
</template>


<script>

import ViewMail from './viewMail.vue'
import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'


export default {
  props: ['messages','folder','numberOfPages'],
  data: function (){
    return {
      dialog: false,
      hidden: true,
      hidden2: true,
      hidden3: true,
      disable: false,
      page: 1,
      n:"",
      indexFolder : '' ,
      sort: null,
      filter: null,
      filtername : null,
      items: ['none','subject','body','date','priority'],
      mail:"",
      delete_retrieve : "delete",
      importanceList: ['1','2','3','4'],
      importanceList2: ['low','medium','Important','veryImportant'],
      importance: '',
      search: null,
      searchQuery: null,
      isSpam:{},
      moveDialog: false,
      AllFolders: [],
      selectedFolder: null,
      moveingMessage: null,
      selectedMessages: [],
      singleMove: false,
      BulkMove: false,
    }
  },
  mounted () {
      console.log(this.folder);
      console.log("dd");
      for(let message in this.messages){
        console.log("messageeeeeeeeeeeeeeeee:");
        console.log("message:",this.messages[message].body);
        this.messages[message].spam=true;
      }
      console.log(this.messages);
      console.log("Number of pages: " +this.numberOfPages);
  },
  components : {
    ViewMail
  },
  methods : {
      move() {
        if (this.singleMove) {
          this.moveMessage();
          this.singleMove = false;
        } else {
          for (let i = 0; i < this.selectedMessages.length; i++) {
            if (this.selectedMessages[i] === true) {
              this.moveingMessage = this.messages[i];
              this.moveMessage();
            }
          }
          this.selectedMessages = [];
          this.bulkMove = false;
          this.refresh(this.indexFolder);
        }
      },
      startMove(index) {
        this.moveDialog = true;
        this.selectedFolder = null;
        this.moveingMessage = this.messages[index];
        this.getAllFolders();
        console.log(this.AllFolders);
        this.singleMove = true;
      },
      moveMessage() {
        console.log(this.moveingMessage);
        const folder = this.AllFolders.find(folder => folder.name === this.selectedFolder);
        axios.put('http://localhost:8080/folders/'+`${folder.id}`+'/emails/'+`${this.moveingMessage.id}`,{},{
          headers: {
            authorization: `${localStorage.getItem('token')}`
          }
        }).then(Response => {
          const Data = Response.data;
        });
        this.moveDialog = false;
        this.refresh(this.indexFolder);
      },
      startBulkMove(){
        this.moveDialog = true;
        this.selectedFolder = null;
        this.bulkMove = true;
        this.getAllFolders();
        console.log(this.AllFolders);
      },
      BulkDelete(){
        for (let i = 0; i < this.selectedMessages.length; i++) {
          if (this.selectedMessages[i] === true) {
            this.deleteOrrestore(this.messages[i]);
          }
        }
        this.selectedMessages = [];
        this.refresh(this.indexFolder);
      },
      getAllFolders() {
        axios.get('http://localhost:8080/folders' ,{
          headers: {
            authorization: `${localStorage.getItem('token')}`
          }
        }).then(Response => {
          const Data = Response.data;
          this.AllFolders = Data;
          console.log(this.AllFolders);
        });
      },
      checkFolder(){
        if(this.folder == "Inbox"){
          this.indexFolder = 0 ;
          this.delete_retrieve = "delete";
        }
        else if(this.folder == "Sent"){
          this.indexFolder = 4;
          this.delete_retrieve = "delete";

        }
        else if(this.folder == "Trash"){
          this.indexFolder = 2;
          this.delete_retrieve = "retrieve";
        }
        else {
          this.indexFolder = 3;
          this.delete_retrieve = "delete";
        }
      },

      deleteOrrestore(message){
        if(message.deleted === false){
          this.deleteEmail(message);
        }
        else {
          this.restoreEmail(message);
        }
        this.refresh(this.indexFolder);
      },
      deleteEmail(message) {
        console.log(message);
        console.log(this.indexFolder);
        axios.put('http://localhost:8080/folders/trash/emails/'+`${message.id}`,{},{
            headers: {
                authorization: `${localStorage.getItem('token')}`
            },
        }).then(Response=>{
            const Data = Response.data;
        });
        this.refresh(this.indexFolder);
      },
      restoreEmail(message) {
        console.log(message);
        console.log(this.indexFolder);
        axios.put('http://localhost:8080/folders/trash/emails/'+`${message.id}`+'/restore',{},{
            headers: {
                authorization: `${localStorage.getItem('token')}`
            },
        }).then(Response=>{
            const Data = Response.data;
        });
        this.refresh(this.indexFolder);
      },
      refresh(index) {
        this.$emit('refresh',index);
      },
      openMessage(n) {
        console.log("ok i will show this mess" + n)
        this.mail = this.messages[n];
      },
       
      changeThePage(){
        this.$emit('changePage',this.page);
      },
      
      applySort(){
        console.log(this.sort);
        console.log(this.folder);
        this.checkFolder();
        this.sortandFilter();
      },

      applyOperations(){
        console.log(this.filter);
        console.log(this.filtername);
        console.log("priority ",this.importance);
        console.log(this.sort);
        console.log(this.search);
        console.log(this.searchQuery);
        this.$emit('applyFilters',{
          filter: this.filter,
          filtername: this.filtername,
          importance: this.importance,
          indexFolder: this.indexFolder,
          sort: this.sort,
          search: this.search,
          searchQuery: this.searchQuery,
        });         
        this.refresh(this.indexFolder);
      },

      cancelFilters(){
        this.filter = null;
        this.filtername = null;
        this.importance = null;
        this.sort = null;
        this.search = null;
        this.searchQuery = null;
        this.hidden = true;
        this.hidden2 = true;
        this.hidden3 = true;
        this.applyOperations();
      },
  }
}
</script>

<style scoped>
.message-gallery {
  margin-top: 80px;
}

.message-card {
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
  margin-bottom: 16px;
  border-radius: 10px;
  background-color: #ebf1fc;
  padding-top: 8px;
}

.message-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  transform: scale(1.05);
}

.message-image {
  margin-left: 8px;
  width: 50px;
  /* object-fit: cover; */
}

.message-info {
  padding: 16px;
  color: black !important;
}

.message-info-item {
  margin-bottom: 8px;
}

.message-action {
  position: absolute;
  top: 8px;
  right: 8px;
}

.message-spam {
  position: absolute;
  top: 8px;
  right: 25px;
}
.cont {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  bottom: 50px;
  left: 50%;
  transform: translate(-50%, 0);
}

.text-bottom {
  display: flex;
  justify-content: center !important;
  margin-top: 20px;
}

.MailsBar {
  padding: 10px;
  height: 80px !important;
  border-radius: 0 0 30px 30px !important;
}
</style>