<template>
  <v-container class="py-15 px-6 sendEmail" fluid>
    <v-row align="center" justify="center">
      <v-card color="#ebf1fc" width="600px">
        <v-card-text>
          <v-row v-if="isSpam" align="center" justify="center">
            <v-alert class="mx-2" color="red" dark dismissible elevation="2" icon="mdi-alert-circle-outline" outlined
              type="error">
              Spam Alert!
            </v-alert>
          </v-row>
          <v-row>
            <v-col>
              <v-card-title primary-title class="layout justify-center" color="red">
                <h2 style="color: #071551;">{{ mail.subject }}</h2>
              </v-card-title>
              <v-card-text class="justify-center">
                <v-col>
                  <div style="float: right;">
                    {{ mail.sendDate.substring(0, 10) }}
                    <br>
                    <span style=" font-weight: bold;">{{ this.priority }}</span>
                  </div>
                </v-col>
                <v-col>
                  <span style="font-size: large; color: #071551; font-weight: bold;">From: </span> 
                  <span style="color: black;">{{ mail.fromUserId }}</span>
                </v-col>
                <v-col>
                  <span style="font-size: large; color: #071551; font-weight: bold;">To: </span> 
                  <span style="color: black;">{{receiversNames }}</span>
                </v-col>
                <v-col>
                  <br>
                  <span style="font-size: large; color: #071551; font-weight: bold;">Body: </span> 
                  <span style="color: black;">{{ mail.body }}</span>
                </v-col>
                <v-col v-if="mail.attachments[0]">
                  <span style="font-size: large; color: #071551; font-weight: bold;">Attachments: </span> 
                  <v-chip v-for="(attachment, index) in mail.attachments[0].paths" :key="index" class="me-2 mt-2"
                    color="#071551c5" @click="download(attachment)">
                    <a :href="`http://localhost:8080/static/${attachment.split('\\').pop().split('/').pop()}`" target="_blank" :download="getFileName(attachment)"
                      style="color: white; text-decoration: none; font-size: 11px;">
                      <v-icon small>mdi-paperclip</v-icon>
                      {{ getFileName(attachment) }}</a>
                  </v-chip>
                </v-col>
                <v-col align="center" justify="center" >
                  <br>
                  <v-btn color="#071551" @click="handleSummarize" dark>
                    <v-icon>mdi-text-box-outline</v-icon>
                    <span class="mx-2">Summarization</span>
                  </v-btn>
                </v-col>
                <v-col v-if="showSummarization" >
                  <span style="font-size: large; color: #071551; font-weight: bold;">Summarization: </span> 
                  <span style="color: black;">{{ summary }}</span>
                </v-col>
              </v-card-text>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios'
export default {
  props: ['mail'],
  data: () => ({
    receiversNames: '',
    priority: '',
    isSpam: false,
    showSummarization: false,
    summary: 'Loading...',
  }),
  methods: {
    formattedRecievers() {
      return this.mail.to.map((reciever) => reciever.name).join(', ');
    },
    getPriority() {
      switch (this.mail.priority) {
        case 1:
          return 'Low';
        case 2:
          return 'Medium';
        case 3:
          return 'Important';
        case 4:
          return 'Very Important';
        default:
          return 'Low';
      }
    },
    getFileName(path) {
      path = decodeURI(path);
      path = path.split('\\').pop().split('/').pop();
      if (path.split('_').length > 1)
        path = path.split('_').slice(1).join('_');
      return path;
    },
    async handleSummarize() {
      event.stopPropagation();
      this.showSummarization = !this.showSummarization;
      await axios.post("https://api-inference.huggingface.co/models/facebook/bart-large-cnn",
        {
          inputs: this.mail.body,
        })
        .then(Response => {
          const Data = Response.data;
          console.log(Data);
          this.summary = Data[0].summary_text;
        });
    },
    async checkSpam() {
      console.log("messageeeeeeeeeeeeeeeee:");
      console.log("message:", this.mail.body);
      let message_id = this.mail.id;
      if (this.mail.body.length > 10) {
        await axios.post('https://api-inference.huggingface.co/models/mshenoda/roberta-spam',
          {
            inputs: this.mail.body,
          }
        ).then(Response => {
          const Data = Response.data;
          let label_1 = {};
          if (Data[0][0].label === "LABEL_1")
            label_1 = Data[0][0]
          else
            label_1 = Data[0][1]
          console.log(Data, label_1);
          this.isSpam = label_1.score > 0.5;
        });
      }
      console.log(this.isSpam);
    },
    download(data) {
      // prevent other onclick events
      event.stopPropagation();
      console.log("download", data);
      // download file from local pc
      // window.open(data.split('///').pop(), '_blank');
    }
  },
  async beforeMount() {
    await this.checkSpam();
  },
  mounted() {
    console.log(this.mail, "maillllllllllllllllllll");
    this.receiversNames = this.formattedRecievers();
    this.priority = this.getPriority();
    this.showSummarization = false;
    this.summary = 'Loading...';
  },
  // on every call of this component
  watch: {
    mail: {
      handler: async function (val, oldVal) {
        this.receiversNames = this.formattedRecievers();
        this.priority = this.getPriority();
        this.showSummarization = false;
        this.summary = 'Loading...';
        await this.checkSpam();
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.sendEmail {
  margin-bottom: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  align-self: center;
  flex-direction: column;
  backdrop-filter: blur(1px);
  height: 100%;
}
.col {
  padding: 0 15px !important;
}
</style>
