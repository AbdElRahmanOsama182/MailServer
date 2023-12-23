<template>
  <v-card>
    <v-toolbar
        flat
        dark
        prominent
        height="180"
        src="..\assets\maintittle5.jpg"
    >
      
      <v-toolbar-title><h1>welcome {{username}}</h1></v-toolbar-title>
    </v-toolbar>
    <v-tabs vertical 
        dark
        background-color="#2d3142"
        v-model='tab'
        >
        <v-sheet
        dark
        color="#2d3142"
        class="pa-4"
        >
        <v-avatar
          class="my-3 mx-16"
          size="64"
          ali
        ><img src="..\assets\user.png" alt="user_photo" /></v-avatar>

            <div class="mb-3 ">{{emailAddress}}</div>
            <v-divider></v-divider>
        </v-sheet>

        <v-tab href='#sendNewEmail'>
            <v-icon left> mdi-email-plus-outline </v-icon>
            Send Email
        </v-tab>

        <v-tab href='#inbox' @click="getInboxEmails">
            <v-icon left>mdi-inbox-arrow-down</v-icon>
            Inbox
        </v-tab>

        <v-tab href='#sent' @click="getSentEmails">
            <v-icon left>mdi-send</v-icon>
            Sent
        </v-tab>

        <v-tab href='#trash' @click="getTrashEmails">
            <v-icon left>mdi-trash-can-outline</v-icon>
            Trash
        </v-tab>

        <v-tab href='#draft' @click="getDraftEmails">
            <v-icon left>mdi-file-outline</v-icon>
            Draft
        </v-tab>

        <v-tab href='#contacts' @click="getContacts">
            <v-icon left>mdi-contacts</v-icon>
            Contacts
        </v-tab>

        <v-tab @click="logout">
            <v-icon left>mdi-logout-variant</v-icon>
            Log-Out
        </v-tab>
        
        
        <!-- this for send new email -->
        <v-tab-item value='sendNewEmail'>
            <SendNewEmail :senderEmailAddress="emailAddress"/>
        </v-tab-item>

        <!-- this for inbox -->
        <v-tab-item value='inbox'>
            <ShowEmails :messeages = "inboxMails" folder = "Inbox" :numberOfPages= "inboxNumPages" @refresh = "refresh($event)"></ShowEmails>
        </v-tab-item>

        <!-- this for send -->
        <v-tab-item value='sent'>
            <ShowEmails :messeages = "sentMails" folder = "Sent" :numberOfPages= "sentNumPages" @refresh = "refresh($event)"></ShowEmails>
        </v-tab-item>

        <!-- this for trash -->
        <v-tab-item value='trash'>
            <ShowEmails :messeages = "trashMails" folder = "Trash" :numberOfPages= "trashNumPages" @refresh = "refresh($event)"></ShowEmails>
        </v-tab-item>

        <!-- this for draft -->
        <v-tab-item value="draft">
            <ShowEmails :messeages = "DraftMails"  folder = "Draft"  :numberOfPages= "DraftNumPages" @refresh = "refresh($event)"></ShowEmails>
        </v-tab-item>

        <!-- this for contacts -->
        <v-tab-item value="contacts">
            <contact :contacts = "contacts" :numberOfPages= "contactsNumPages"></contact>
        </v-tab-item>


        
    </v-tabs>
  </v-card>
</template>

<script>

//import our component
import Contact from '../components/contact.vue'
import SendNewEmail from '../components/sendNewEmail.vue'
import ShowEmails from '../components/veiwMails.vue'

import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'


export default {
    data: () => ({
        tab : "sendNewEmail",
        username : '',
        emailAddress : '',
        inboxMails : [],
        inboxNumPages : 0 ,
        sentMails : [],
        sentNumPages : 0 ,
        trashMails : [],
        trashNumPages : 0 ,
        DraftMails : [],
        DraftNumPages : 0 ,
        contacts : [],
        contactsNumPages : 0 ,
    }),
    components : {
        Contact,
        SendNewEmail,
        ShowEmails,
    },
    mounted () {
        axios.get('http://localhost:8080/api/getUser',{
        }).then(Response=>{
            const Data = Response.data;
            this.username = Data ;
        });
        axios.get('http://localhost:8080/api/getUseraddress',{
        }).then(Response=>{
            const Data = Response.data;
            this.emailAddress = Data ;
        });
    },
    methods : {
        getInboxEmails : function(){
            axios.get('http://localhost:8080/api/SetEmailsToShow',{
                params: {
                    FolderIndex : 0
                }
            }).then(Response=>{
                const Data = Response.data;
                this.inboxMails = Data ;
                axios.get('http://localhost:8080/api/EmailsNumberOfPages',{
                    }).then(Response=>{
                        const Data = Response.data;
                        this.inboxNumPages = Data ;
                });
            });  
        },
        getSentEmails : function(){
            axios.get('http://localhost:8080/api/SetEmailsToShow',{
                params: {
                    FolderIndex : 4
                }
            }).then(Response=>{
                const Data = Response.data;
                this.sentMails = Data ;
                axios.get('http://localhost:8080/api/EmailsNumberOfPages',{
                    }).then(Response=>{
                        const Data = Response.data;
                        this.sentNumPages = Data ;
                });
            });  
        },
        getTrashEmails : function(){
            axios.get('http://localhost:8080/api/SetEmailsToShow',{
                params: {
                    FolderIndex : 2
                }
            }).then(Response=>{
                const Data = Response.data;
                this.trashMails = Data ;
                axios.get('http://localhost:8080/api/EmailsNumberOfPages',{
                    }).then(Response=>{
                        const Data = Response.data;
                        this.trashNumPages = Data ;
                });
            });  
        },
        getDraftEmails : function(){
            axios.get('http://localhost:8080/api/SetEmailsToShow',{
                params: {
                    FolderIndex : 3
                }
            }).then(Response=>{
                const Data = Response.data;
                this.DraftMails = Data ;
                axios.get('http://localhost:8080/api/EmailsNumberOfPages',{
                    }).then(Response=>{
                        const Data = Response.data;
                        this.DraftNumPages = Data ;
                });
            });  
        },
        getContacts : function(){
            axios.get('http://localhost:8080/api/GetContactPage',{
                params: {
                    PageNumber : 1
                }
            }).then(Response=>{
                const Data = Response.data;
                this.contacts = Data ;
                axios.get('http://localhost:8080/api/ContactsNumberOfPages',{
                    }).then(Response=>{
                        const Data = Response.data;
                        this.contactsNumPages = Data ;
                });
            });

        },
        logout : function(){
            axios.get('http://localhost:8080/api/logout',{})
            .then(Response=>{
                this.$router.push({name:'login'});
            });
        },
        sleep(ms) {
        return new Promise(
            resolve => setTimeout(resolve, ms)
        );
        },
        async refresh(indexFolder) {
            await this.sleep(60);
            if(indexFolder == 0){
                this.getInboxEmails() ;
                console.log(indexFolder);
            }
            else if(indexFolder == 4){
                this.getSentEmails();
                console.log(indexFolder);
            }
            else if(indexFolder == 2){
                this.getTrashEmails();
                console.log(indexFolder);
            }
            else {
                this.getDraftEmails();
                console.log(indexFolder);
            }
        },
    }
}
</script>

<style>

</style>