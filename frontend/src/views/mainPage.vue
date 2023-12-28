<template>
  <div class="main-container">
    
    <div class="tab-content">
      <div v-if="currentTab === 'sendNewEmail'">
        <SendNewEmail :senderEmailAddress="emailAddress"/>
      </div>
      <div v-else-if="currentTab === 'inbox'">
        <ShowEmails :messages="inboxMails" folder="Inbox" :numberOfPages="inboxNumPages" @refresh="refresh" @applyFilters="applyFilters" @changePage="changePage" />
      </div>
      <div v-else-if="currentTab === 'sent'">
        <ShowEmails :messages="sentMails" folder="Sent" :numberOfPages="sentNumPages" @refresh="refresh" @applyFilters="applyFilters" @changePage="changePage" />
      </div>
      <div v-else-if="currentTab === 'trash'">
        <ShowEmails :messages="trashMails" folder="Trash" :numberOfPages="trashNumPages" @refresh="refresh" @applyFilters="applyFilters" @changePage="changePage" />
      </div>
      <div v-else-if="currentTab === 'draft'">
        <ShowEmails :messages="DraftMails" folder="Draft" :numberOfPages="DraftNumPages" @refresh="refresh" @applyFilters="applyFilters" @changePage="changePage" />
      </div>
      <div v-else-if="currentTab === 'folders'">
        <ShowEmails :messages="chosenFolderEmails" folder="folders" :numberOfPages="folderNumPages" @refresh="refresh" />
      </div>
      <div v-else-if="currentTab === 'contacts'">
        <contact :contacts="contacts" :numberOfPages="contactsNumPages" />
      </div>
    </div>
    <FolderOptions class="folderOptions" :folders="folders" v-if="showFolderOptions" @view-folder="handleViewFolder" />
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
      <div class="tab" @click="changeTab('folders'); showFolderOptions=true" :class="{ active: currentTab === 'folders' }">
        <i class="icon mdi mdi-folder"></i>
        {{choosenFolder}}
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
import axios from 'axios';
import FolderOptions from '../components/FolderOptions.vue';
import Contact from '../components/contact.vue';
import SendNewEmail from '../components/sendNewEmail.vue';
import ShowEmails from '../components/veiwMails.vue';

export default {
  props: ['sort', 'filter', 'filtername', 'importance', 'search', 'searchQuery'],
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
    choosenFolder: 'folders',
    chosenFolderEmails : [],
    folderNumPages: 0,
    Folders: [],
    showFolderOptions: false,
    filterPriority: null,
    filterSubject: null,
    sort: null,
    search: null,
    searchQuery: null,
    Mails: [],
  }),
  components: {
    SendNewEmail,
    ShowEmails,
    Contact,
    FolderOptions,
  },
  mounted() {
    this.getFolders();
    // this.getUserInfo();
    // this.getInboxEmails();
  },
  methods: {
    handleViewFolder({ folder, emails, pages }) {
      this.showFolderOptions = false;
      this.choosenFolder = folder.name;
      this.currentTab = 'folders';
      this.chosenFolderEmails = emails;
      this.inboxNumPages = pages;
      console.log('pages', pages);
    },
    applyFilters(data) {
      this.filterSubject = data.filter === 'subject' ? data.filtername : null;
      this.filterPriority = data.filter === 'importance' ? data.importance : null;
      this.sort = data.sort === null || data.sort === 'none' ? null : data.sort;
      this.search= data.search === null || data.search === 'none' ? null : data.search;
      this.searchQuery = data.searchQuery === null || data.searchQuery === 'none' ? null : data.searchQuery;
      console.log("in main page");
      console.log(this.filterPriority);
      console.log(this.filterSubject);
      console.log(this.sort);
      console.log(this.search);
      console.log(this.searchQuery);
      this.refresh(data.indexFolder);
    },
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
    getEmailsByFolderName(PageNumber=1) {
      axios.get('http://localhost:8080/folders/'+this.currentTab+'/emails', {
        headers: {
          authorization: `${localStorage.getItem('token')}`
        },
        params: {
          sort: this.sort,
        filterSubject: this.filterSubject,
        filterPriority: this.filterPriority,
        searchType: this.search,
        searchValue: this.searchQuery,
        page: PageNumber,

        },
      }).then(Response=>{
        const Data = Response.data;
        if (this.currentTab === 'inbox') {
          this.inboxMails = Data.emails ;
          this.inboxNumPages = Data.pages ;
        } else if (this.currentTab === 'sent') {
          this.sentMails = Data.emails ;
          this.sentNumPages = Data.pages ;
        } else if (this.currentTab === 'trash') {
          this.trashMails = Data.emails ;
          this.trashNumPages = Data.pages ;
        } else if (this.currentTab === 'draft') {
          this.DraftMails = Data.emails ;
          this.DraftNumPages = Data.pages ;
        }
        

      }
      );
      console.log('Getting folder: ', this.currentTab);
      console.log(this.Mails);


    },
    async getInboxEmails() {
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
    getSentEmails() {
      console.log("in get sent emails");
      console.log(this.filterPriority);
      console.log(`${localStorage.getItem('token')}`);
      axios.get('http://localhost:8080/folders/sent/emails', {
        
      // }, {
        headers: {
          authorization: `${localStorage.getItem('token')}`
        },
        params: {
          sort: this.sort,
        filterSubject: this.filterSubject,
        filterPriority: this.filterPriority,
        searchType: this.search,
        searchValue: this.searchQuery,
        },
      }).then(Response=>{
        const Data = Response.data;
        this.sentMails = Data ;
      }
      );

        console.log("Sent Mails")
        console.log(this.sentMails);
    },
    async getTrashEmails() {
      try {
        const response = await axios.get('http://localhost:8080/api/SetEmailsToShow', {
          params: {
            FolderIndex: 2,
          },
        });
        this.trashMails = response.data;

        const numPagesResponse = await axios.get('http://localhost:8080/api/EmailsNumberOfPages');
        this.trashNumPages = numPagesResponse.data;
      } catch (error) {
        console.error('Error fetching trash emails', error);
      }
    },
    async getDraftEmails() {
      try {
        const response = await axios.get('http://localhost:8080/api/SetEmailsToShow', {
          params: {
            FolderIndex: 3,
          },
        });
        this.DraftMails = response.data;

        const numPagesResponse = await axios.get('http://localhost:8080/api/EmailsNumberOfPages');
        this.DraftNumPages = numPagesResponse.data;
      } catch (error) {
        console.error('Error fetching draft emails', error);
      }
    },
    async getFolders() {
      try {
        const response = await axios.get('http://localhost:8080/folders', {
          headers: {
            authorization: `${localStorage.getItem('token')}`,
          },
        });
        this.folders = response.data.filter(folder => {
          const name = folder.name.toLowerCase();
          return name !== 'inbox' && name !== 'draft' && name !== 'sent' && name !== 'trash';
        });
      } catch (error) {
        console.error('Error fetching folders', error);
      }
    },
    async refresh(indexFolder) {
      console.log('refreshing');
      await this.sleep(60);
      this.getEmailsByFolderName();
    },
    changeTab(tab) {
      this.showFolderOptions = false;
      this.choosenFolder= 'folders';
      this.currentTab = tab;
      if (this.currentTab !== 'sendNewEmail' && this.currentTab !== 'contacts')
        this.getEmailsByFolderName();
      console.log(this.currentTab);
    },
    logout() {
        this.$router.push({ name: 'login' });
    },
    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    },
    changePage(data) {
      this.getEmailsByFolderName(data);
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
background-color: white;
display: flex;
flex-direction: column;
align-items: center;
width: 100%;
padding: 0 5%;
align-self: center;
}

.folderOptions {
  position: absolute;
  top: 50%;
  justify-content: center;
  z-index: 10;
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
color: #071551;
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
border-radius: 30px 30px 0 0;
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
</style>