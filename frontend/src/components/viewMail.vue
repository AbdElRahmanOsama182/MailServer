<template>
  <v-container class="py-15 px-6 sendEmail" fluid>
    <v-row align="center" justify="center">
        <v-card color="#BFD7ED" width="600px">
          <v-card-text>
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
                    <v-card>
                      <v-chip v-for="(attachment, index) in mail.attachments" :key="index" class="me-2 mt-2" color="primary">
                        {{ attachment.name }}
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
export default {
  props: ['mail'],
  data: () => ({
    receiversNames: '',
  }),
  methods: {
    formattedRecievers() {
      return this.mail.to.map((reciever) => reciever.name).join(', ');
    },
  },
  mounted() {
    console.log(this.mail);
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
}

.col {
  padding: 0 15px !important;
}
</style>
