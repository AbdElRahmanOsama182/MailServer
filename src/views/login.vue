<template>
    <v-img
        src="..\assets\background.jpg" 
        height="100" 
    >
    
        <v-card 
            class="mx-auto my-10"
            max-width="400"
        >
                <v-card-title primary-title class="layout justify-center">
                    <h1>LOG-IN</h1>
                </v-card-title>

                <v-divider></v-divider>

                <v-card-text primary-title class="justify-center">
                    <v-col>
                        <h2>E-mail</h2>
                        <v-text-field
                            v-model="EmailAddress"
                            :error-messages="emailErrors"
                            required
                            placeholder="Enter your email here"
                            @input="$v.EmailAddress.$touch()"
                            @blur="$v.EmailAddress.$touch()"
                        ></v-text-field>
                    </v-col>

                    <v-col>
                        <h2>Password</h2>
                        <v-text-field
                            v-model="password"
                            :error-messages="passwordErrors"
                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                            :rules="[rules.required, rules.min]"
                            :type="showPassword ? 'text' : 'password'"
                            name="input-10-1"
                            hint="At least 8 characters"
                            counter
                            required
                            placeholder="Enter your password here"
                            @input="$v.password.$touch()"
                            @blur="$v.password.$touch()"
                            @click:append="showPassword = !showPassword"
                        ></v-text-field>
                    </v-col>

                    <v-divider></v-divider>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn to="/signup">
                            sign-up
                        </v-btn>
                        <v-spacer></v-spacer>


                        <v-btn 
                        color="#FF9062" 
                        @click="CheckLogin"
                        >
                            log-in
                        </v-btn>
                        
                        <v-snackbar
                            v-model="snackbar"
                        >
                            {{ message }}

                            <template v-slot:action="{ attrs }">
                                <v-btn
                                color="blue"
                                text
                                v-bind="attrs"
                                @click="snackbar = false"
                                >
                                Close
                                </v-btn>
                            </template>
                        </v-snackbar>
                        
                        
                        <v-spacer></v-spacer>
                </v-card-actions>
            </v-card-text>

        </v-card>
  
  
  
    </v-img>
</template>

<script>

import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import { validationMixin } from "vuelidate";
import { required, maxLength, email } from "vuelidate/lib/validators";

export default {
    mixins: [validationMixin],
    validations: {
        EmailAddress: { required, email },
        password: { required },
    },
    data: () => ({
        EmailAddress : '',
        password : '',
        showPassword : false ,
        snackbar : false ,
        message : '',
        rules: {
        required: value => !!value || 'Required.',
        min: v => v.length >= 8 || 'Min 8 characters',
        emailMatch: () => (`The email and password you entered don't match`),
        },   
    }),
    methods : {
        CheckLogin : function(){
            axios.get('http://localhost:8080/api/login',{
                params: {
                    Emailaddress : this.EmailAddress,
                    password : this.password,
                }
            }).then(Response=>{
                const Data = Response.data;
                this.message = Data ;
                this.snackbar = true ;
                if(this.message == "Successfully Log in"){
                    setTimeout(this.gomainpage, 1000);
                }
            });
        },
        gomainpage(){
            this.$router.push({name:'mainpage'});
        }
    },
    computed: {
        emailErrors() {
        const errors = [];
        if (!this.$v.EmailAddress.$dirty) return errors;
        !this.$v.EmailAddress.email && errors.push("Must be valid e-mail");
        !this.$v.EmailAddress.required && errors.push("E-mail is required");
        return errors;
        },
        passwordErrors() {
        const errors = [];
        if (!this.$v.password.$dirty) return errors;
        !this.$v.password.required && errors.push("Password is required");
        return errors;
        }
    },
}
</script>

<style>
h1,.v-btn,h2{
    font-family: 'JetBrains Mono';
}

.v-btn{
    margin: 10px;
}

</style>