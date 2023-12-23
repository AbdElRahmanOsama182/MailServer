<template>
    <v-container
    class="py-8 px-6"
    fluid
    >
        <v-img
          src="..\assets\maintittle6.jpg" 
          height="100" 
        >
        <h1 class="white--text mt-9 mx-6">{{tittle}}</h1>
        </v-img>
        <v-row>
          <v-col cols="12">
          <v-list three-line>
            <v-card>
                  <v-list-item v-for="(messeage,i) in messeages"
                  :key=i
                  @click="show(i)"
                  >
                    
                    <v-list-item-avatar  tile>
                      <v-img src="..\assets\mail.png"></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                      <v-list-item-title>this email sender is : {{ messeage.sender }}</v-list-item-title>

                      <v-list-item-subtitle>
                        subject : {{messeage.subject}}
                      </v-list-item-subtitle>
                      <v-list-item-subtitle>
                        body : {{messeage.body}}
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
            </v-card>
          
              </v-list>
          </v-col>
        </v-row>
      </v-container>
</template>

<script>


export default {
    props: ['messeages','tittle'],
    data: function (){
      return{}
    },
    mounted () {
        console.log(this.messeages);

    },
    methods : {
        show : function(i){
          console.log(i);
        }
    }
}
</script>

<style>

</style>



