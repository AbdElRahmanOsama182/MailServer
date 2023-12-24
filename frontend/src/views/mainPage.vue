<template>
    <div class="main-container">
      <div class="header">
        <div class="header-content">
          <h1 class="header-title">Welcome {{ username }}</h1>
          <div class="user-info">
            <img src="..\assets\user.png" alt="user_photo" class="user-photo" />
            <div class="user-email">{{ emailAddress }}</div>
          </div>
        </div>
      </div>
      <div class="tab-content">
        <div v-if="currentTab === 'sendNewEmail'">
          <SendNewEmail :senderEmailAddress="emailAddress"/>
        </div>
        <div v-else-if="currentTab === 'inbox'">
          <ShowEmails :messages="inboxMails" folder="Inbox" :numberOfPages="inboxNumPages" @refresh="refresh" />
        </div>
        <div v-else-if="currentTab === 'sent'">
          <ShowEmails :messages="sentMails" folder="Sent" :numberOfPages="sentNumPages" @refresh="refresh" />
        </div>
        <div v-else-if="currentTab === 'trash'">
          <ShowEmails :messages="trashMails" folder="Trash" :numberOfPages="trashNumPages" @refresh="refresh" />
        </div>
        <div v-else-if="currentTab === 'draft'">
          <ShowEmails :messages="DraftMails" folder="Draft" :numberOfPages="DraftNumPages" @refresh="refresh" />
        </div>
        <div v-else-if="currentTab === 'contacts'">
          <contact :contacts="contacts" :numberOfPages="contactsNumPages" />
        </div>
      </div>
      <div class="tabs">
        <div class="tab" @click="changeTab('sendNewEmail')" :class="{ active: currentTab === 'sendNewEmail' }">
          <i class="icon mdi mdi-email-plus-outline"></i>
          Send New Email
        </div>
        <div class="tab" @click="changeTab('inbox')" :class="{ active: currentTab === 'inbox' }">
          <i class="icon mdi mdi-inbox-arrow-down"></i>
          Inbox
        </div>
        <div class="tab" @click="changeTab('sent')" :class="{ active: currentTab === 'sent' }">
          <i class="icon mdi mdi-send"></i>
          Sent
        </div>
        <div class="tab" @click="changeTab('trash')" :class="{ active: currentTab === 'trash' }">
          <i class="icon mdi mdi-trash-can-outline"></i>
          Trash
        </div>
        <div class="tab" @click="changeTab('draft')" :class="{ active: currentTab === 'draft' }">
          <i class="icon mdi mdi-file-outline"></i>
          Draft
        </div>
        <div class="tab" @click="changeTab('contacts')" :class="{ active: currentTab === 'contacts' }">
          <i class="icon mdi mdi-contacts"></i>
          Contacts
        </div>
        <div class="tab" @click="logout">
          <i class="icon mdi mdi-logout-variant"></i>
          Log-Out
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Vue from 'vue'
  import VueAxios from 'vue-axios'
  import axios from 'axios'
  import SendNewEmail from '../components/sendNewEmail.vue'
  import ShowEmails from '../components/veiwMails.vue'
  import Contact from '../components/contact.vue'
  
  export default {
    data: () => ({
      currentTab: 'sendNewEmail',
      username: '',
      emailAddress: '',
      inboxMails: [],
      inboxNumPages: 0,
      sentMails: [],
      sentNumPages: 0,
      trashMails: [],
      trashNumPages: 0,
      DraftMails: [],
      DraftNumPages: 0,
      contacts: [],
      contactsNumPages: 0,
    }),
    components: {
      SendNewEmail,
      ShowEmails,
      Contact,
    },
    mounted() {
      this.getUserInfo();
      this.getInboxEmails();
    },
    methods: {
      async getUserInfo() {
        try {
          const response = await axios.get('http://localhost:8080/api/getUser');
          this.username = response.data;
        } catch (error) {
          console.error('Error fetching user info', error);
        }
  
        try {
          const response = await axios.get('http://localhost:8080/api/getUseraddress');
          this.emailAddress = response.data;
        } catch (error) {
          console.error('Error fetching user address', error);
        }
      },
      async getInboxEmails() {
        try {
          const response = await axios.get('http://localhost:8080/api/SetEmailsToShow', {
            params: {
              FolderIndex: 0,
            },
          });
          this.inboxMails = response.data;
  
          const numPagesResponse = await axios.get('http://localhost:8080/api/EmailsNumberOfPages');
          this.inboxNumPages = numPagesResponse.data;
        } catch (error) {
          console.error('Error fetching inbox emails', error);
        }
      },
      async refresh(indexFolder) {
        await this.sleep(60);
  
        if (indexFolder === 0) {
          this.getInboxEmails();
        } else if (indexFolder === 4) {
          this.getSentEmails();
        } else if (indexFolder === 2) {
          this.getTrashEmails();
        } else {
          this.getDraftEmails();
        }
      },
      changeTab(tab) {
        this.currentTab = tab;
        console.log(this.currentTab);
      },
      logout() {
        axios.get('http://localhost:8080/api/logout', {}).then(() => {
          this.$router.push({ name: 'login' });
        });
      },
      sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
      },
    },
  };
  </script>
  
  <style scoped>
  .active {
    color: #fff;
    font-weight: 700;
    box-shadow: #fff 0 -2px 0 inset;
  }
  template {
    height: 100%;
  }
.main-container {
  background-color: #6d6f7b;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 5%;
  align-self: center;
}

.content-container {
  max-height: 100vh;
}

.header {
  position: relative;
  width: 100%;
}

.header-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-photo {
  width: 50px;
  height: 50px;
  margin-right: 1rem;
}

.tab-content {
  width: 100%;
}

.tabs {
  display: flex;
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: #071551;
  z-index: 1;
  justify-content: space-around;
  border-radius: 2.5rem 2.5rem 0 0;
  padding: 1rem 0.5rem 2rem 0.5rem;
}

.tab {
  margin-right: 1rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
  color: white;
}

.icon {
  margin-right: 0.5rem;
}

/* Add more styles as needed */
</style>