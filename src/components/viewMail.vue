<template>
    <v-container>
        <v-card 
            class="mx-auto my-3"
            max-width="1000"
            color="#1B262C"
        >
           <v-card-title primary-title 
           class="layout justify-center"
           color="red">
                <h2>{{mail.subject}}</h2>
            </v-card-title>

            <v-divider color="white"></v-divider>


            <v-card-text class="justify-center">
                    <v-col>
                        
                        <h2 class="font-italic">From : </h2>
                        <v-card>
                            <v-card-subtitle class="font-italic">
                                <span class="text--primary"> {{mail.sender}} </span>
                            </v-card-subtitle>
                        </v-card>
                           
                    </v-col>
                    <v-col>
                        <h2 class="font-italic">To : </h2>
                        <v-card>
                            <v-card-subtitle class="font-italic">
                                <span class="text--primary"> {{mail.recievers[0]}}</span>
                            </v-card-subtitle>
                        </v-card>      
                    </v-col>
                    <v-col>
                        <v-card min-height="200">
                            <v-card-subtitle class="text--primary"> {{mail.body}} </v-card-subtitle>
                        </v-card>
                    </v-col>
                    <v-col>
                    <div class="font-italic">{{mail.importance=="veryImportant" ? 'Very Important' : mail.importance}} - {{mail.date}}</div>
                    </v-col>
            </v-card-text>
        </v-card>
    </v-container>
</template>


<script>
export default {
    props: ['mail'],
    data: () => ({
    }),
    
    methods: {
        
    }
}
</script>
<style scoped>
.theme--light.v-card {
  background-color: #D1D4C9;
}

h2{
  color: #BBE1FA;
}
.font-italic{
    color:#BBE1FA;
}
.theme--light.v-divider {
  border-color: rgb(255, 255, 255);
}
.theme--light.v-list {
  background: #1B262C;
  color: rgba(0, 0, 0, 0.87);
}
.v-btn--round .v-btn__content .v-icon {
  color: #64bbf6;
}
.theme--light.v-application .text--secondary {
  color: #BBE1FA !important;
}
.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled) {
  color: #BBE1FA !important;
}
.theme--light.v-list-item .v-list-item__subtitle {
  color: #BBE1FA;
}
.theme--light.v-tabs-items {
  background-color: #5B5B60;
}

</style>