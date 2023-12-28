<template>
  <v-container class="py-15 px-6 sendEmail" fluid>
    <!-- red announcement if spam -->
    
    <v-row align="center" justify="center">
        <v-card color="#BFD7ED" width="600px">
          <v-card-text>
            <v-row v-if="isSpam" align="center" justify="center">
      <v-alert
        class="mx-2"
        color="red"
        dark
        dismissible
        elevation="2"
        icon="mdi-alert-circle-outline"
        outlined
        type="error"
      >
        This email is spam!
      </v-alert>
    </v-row>
            <v-row>
              <v-col>
                <v-card-title primary-title class="layout justify-center" color="red">
                  <h2>{{ mail.subject }}</h2>
                </v-card-title>

                <v-divider></v-divider>

                <v-card-text class="justify-center">
                  <v-col>
                    <h2 class="font-italic">From : </h2>
                    <v-card>
                      <v-card-subtitle class="font-italic">
                        <span class="text--primary">{{ mail.fromUserId }}</span>
                      </v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col>
                    <h2 class="font-italic">To : </h2>
                    <v-card>
                      <v-card-subtitle class="font-italic">
                        <span class="text--primary">{{ receiversNames }}</span>
                      </v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col>
                      <h2 class="font-italic">Body : </h2>
                    <v-card min-height="200">
                      <v-card-subtitle class="text--primary">{{ mail.body }}</v-card-subtitle>
                    </v-card>
                  </v-col>
                  <v-col>
                      <h2 class="font-italic">Attachments:</h2>
                    <v-card v-if="mail.attachments[0]" >
                      <v-chip v-for="(attachment, index) in mail.attachments[0].paths" :key="index" class="me-2 mt-2" color="primary" @click="download(attachment)">
                        <a :href="`http://localhost:8080/static/${attachment.split('\\').pop().split('/').pop()}`" download style="color: white;">{{  attachment.split('\\').pop().split('/').pop() }}</a>
                      </v-chip>
                    </v-card>
                  </v-col>
                  <v-col>
                    <div class="font-italic">
                      Priority: {{ mail.priority }} - Date: {{ mail.sendDate.substring(0, 10) }}
                    </div>
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
    isSpam: false,
  }),
  methods: {
    formattedRecievers() {
      return this.mail.to.map((reciever) => reciever.name).join(', ');
    },
      async checkSpam(){
        
        console.log("messageeeeeeeeeeeeeeeee:");
          console.log("message:",this.mail.body);
          let message_id = this.mail.id;
          if(this.mail.body.length>10){
          await axios.post('https://api-inference.huggingface.co/models/mshenoda/roberta-spam',
              {
                inputs: this.mail.body,
              }
          ).then(Response=>{
                const Data = Response.data;
                    let label_1={};
                    if(Data[0][0].label==="LABEL_1")
                        label_1=Data[0][0]
                    else
                        label_1=Data[0][1]
                console.log(Data,label_1);
                this.isSpam = label_1.score>0.5;
          });
          }
        console.log(this.isSpam);
      },
      download(data){
        // prevent other onclick events
        event.stopPropagation();
        console.log("download",data);
        // download file from local pc
        // window.open(data.split('///').pop(), '_blank');
        


      }
  },
  async beforeMount() {
    await this.checkSpam();
  },
  mounted() {
    console.log(this.mail,"maillllllllllllllllllll");
    this.receiversNames = this.formattedRecievers();
  },
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
}

.col {
  padding: 0 15px !important;
}
</style>
