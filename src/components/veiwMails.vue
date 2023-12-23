<template>
    <v-container
    class="py-8 px-6"
    fluid
    style="background-color: #5b5b60"
    >
        <h1 class="white--text mt-9 mx-10">{{folder}}</h1>
        <v-row>

           <v-btn
              color="white"
              @click="hidden = !hidden"
            >
            <v-icon
            left
          >
              {{ hidden ? 'mdi-chevron-right' : 'mdi-chevron-left' }}
            </v-icon>Sort
          </v-btn>
            <v-col md="2">
            <v-fab-transition>
              <v-combobox
                v-show="!hidden"
                v-model="sort"
                :items="items"
                label="Sort by"
                dense
              ></v-combobox>
            </v-fab-transition>
            </v-col>
          <v-fab-transition>
            <v-btn
                v-show="!hidden"
                fab
                small
                dark
                color="#2d3142"
                @click="applySort"
                    >
                      <v-icon>mdi-checkbox-marked-circle</v-icon>
            </v-btn>
          </v-fab-transition>

          <v-col md="1"> <div></div></v-col>
          <v-fab-transition> 
          <v-btn
                color="white"
                @click="hidden2 = !hidden2"
              >
              <v-icon
              left
            >
              {{ hidden2 ? 'mdi-chevron-right' : 'mdi-chevron-left' }}
            </v-icon>Filter
          </v-btn>
          </v-fab-transition>

          <v-col
            md="2"
          > 
          <v-fab-transition>
            <v-combobox
              v-show="!hidden2"
              v-model="filter"
              :items="items"
              label="Filter by"
              dense
            ></v-combobox>
          </v-fab-transition>
          </v-col>

          <v-col
            md="2"
          >
          <v-fab-transition>
            <v-text-field
              v-show="!hidden2 && filter!='importance'"
              clearable
              dense
              v-model="filtername"
            >
            </v-text-field>              
          </v-fab-transition> 
          <v-fab-transition> 
          <v-slider span
          v-show="!hidden2 && filter=='importance'"
          v-model="importance"
          :tick-labels="importanceList"
          :max="3"
          step="1"
          ticks="always"
          tick-size="4"
          ></v-slider>
          </v-fab-transition> 
          </v-col> 
          <v-fab-transition>
          <v-btn
              v-show="!hidden2"
              fab
              small
              dark
              color="#2d3142"
              @click="applyFilter"
                  >
                    <v-icon>mdi-checkbox-marked-circle</v-icon>
          </v-btn>
          </v-fab-transition> 

          <v-col cols="12">
          <v-card>
            <v-dialog
                v-model="dialog"
                fullscreen
                hide-overlay
                transition="dialog-bottom-transition"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-list four-line>
                          <v-list-item v-for="(messeage,i) in messeages"
                          :key=i
                          @click="openMessage(i)"
                          v-bind="attrs"
                          v-on="on"
                          >
                            <v-list-item-avatar  tile>
                              <v-img src="..\assets\mail.png"></v-img>
                            </v-list-item-avatar>

                            <v-list-item-content>
                              <v-list-item-title>From : {{ messeage.sender }}</v-list-item-title>

                              <v-list-item-subtitle>
                                To : {{messeage.recievers[0]}}
                              </v-list-item-subtitle>

                              <v-list-item-subtitle>
                                subject : {{messeage.subject}}
                              </v-list-item-subtitle>
                              
                              <v-list-item-subtitle>
                                body : {{messeage.body}}
                              </v-list-item-subtitle>
                            </v-list-item-content>
                            <v-row justify="center" >
                              <v-col md="5">
                                <div class="text--secondary">{{ messeage.date }}</div>
                              </v-col>
                              </v-row>
                              
                              <v-col >
                                <div class="text--secondary">{{ messeage.importance=="veryImportant" ? 'Very Important' : messeage.importance }}</div>
                              </v-col>

                            <v-btn
                              @click.stop="openMessage(messeage)"
                              @click="deleteORretrieve(messeage)"
                              icon
                            ><v-icon>{{ delete_retrieve=="delete" ? 'mdi-delete' : 'mdi-delete-restore' }}</v-icon>
                            </v-btn>
                          </v-list-item>
                          <v-divider></v-divider>
                  
                    </v-list>

        </template>

                <v-card>
                        <v-toolbar
                        color="#2d3142"
                        dark
                        dense
                        flat
                        >
                          <v-btn
                              icon
                              dark
                              @click="dialog = false"
                          >
                              <v-icon>mdi-close</v-icon>
                          </v-btn>
                          <v-toolbar-title><v-icon>mdi-mail</v-icon></v-toolbar-title>
                          <v-spacer></v-spacer>
                        </v-toolbar>

                        <ViewMail :mail="mail" @reply= "reply($event)" ></ViewMail>

                </v-card>
          </v-dialog>
          </v-card>

          </v-col>
        </v-row>

        <div class="text-bottom">
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

import ViewMail from './viewMail.vue'

import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'


export default {
    props: ['messeages','folder','numberOfPages'],
    data: function (){
      return {
        dialog: false,
        hidden: true,
        hidden2: true,
        disable: false,
        page: 1,
        n:"",
        indexFolder : '' ,
        sort: 'null',
        filter: 'null',
        filtername : 'null',
        items: ['null','subject','sender','SenderAddress','date','importance'],
        mail:"",
        delete_retrieve : "delete",
        importanceList: ['1','2','3','4'],
        importanceList2: ['low','medium','Important','veryImportant'],
        importance: '',
      }
    },
    mounted () {
        console.log(this.f);
        console.log(this.messeages);
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
    components : {
      ViewMail
    },
    methods : {
        deleteORretrieve(messeage) {
          console.log(messeage);
          console.log(this.indexFolder);
          if (this.indexFolder === 2) {
            axios.get('http://localhost:8080/api/retriveFromTrash',{
                params: {
                    id : messeage.id,
                }
            }).then(Response=>{
                // axios.get('http://localhost:8080/api/getPage',{
                // params: {
                //     PageNumber : this.page,
                // }
                // }).then(Response=>{
                //   const Data = Response.data;
                //   this.messeages = Data ;
                // });
            });
          }
          else {
            axios.get('http://localhost:8080/api/bulkDelete',{
                  params: {
                      id : messeage.id,
                      indexOfDefaultFolder : this.indexFolder
                  }
              }).then(Response=>{
                // axios.get('http://localhost:8080/api/getPage',{
                // params: {
                //     PageNumber : this.page,
                // }
                // }).then(Response=>{
                //   const Data = Response.data;
                //   this.messeages = Data ;
                // });
              });
          }
          this.refresh(this.indexFolder);
        },
        refresh(index) {
          this.$emit('refresh',index);
        },
        openMessage(n) {
          console.log("ok i will show this mess" + n)
          this.mail = this.messeages[n];
        },
         
        changeThePage(){
          console.log("okokkoko")
          axios.get('http://localhost:8080/api/getPage',{
                params: {
                    PageNumber : this.page
                }
          }).then(Response=>{
                const Data = Response.data;
                this.messeages = Data ;
          });
        },
        
        applySort(){
          console.log(this.sort);
          this.sortandFilter();
        },

        applyFilter(){
            if (this.filter === 'importance'){
              this.filtername = this.importanceList2[this.importance];
              console.log(this.filtername);
            }
          this.sortandFilter();
          console.log(this.filter);
          
        },

        sortandFilter : function(){
          axios.post('http://localhost:8080/api/FilterAndSort?sort='+this.sort,
                [ [ this.filter , this.filtername ] ]
          ).then(Response=>{
                const Data = Response.data;
                this.messeages = Data ;
          });
        },
    }
}
</script>

<style>

.theme--light.v-input input{
  color:#BBE1FA;
}
#input-402{
  color: #084b77;
}
</style>