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
                    <h1>SIGN-UP</h1>
                </v-card-title>

                <v-divider></v-divider>

                <v-card-text primary-title class="justify-center">
                    <v-col>
                    <h2>User-name</h2>
                        <v-text-field
                        v-model="userName"
                        placeholder="Enter your username here"
                        :error-messages="nameErrors"
                        :counter="30"
                        required
                        @input="$v.userName.$touch()"
                        @blur="$v.userName.$touch()"
                        ></v-text-field>
                    </v-col>

                    <v-col>
                    <h2>E-mail</h2>
                        <v-text-field
                          v-model="EmailAddress"
                          :error-messages="emailErrors"
                          placeholder="Enter your email here"
                          required
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
                          placeholder="Enter your password here"
                          hint="At least 8 characters"
                          counter
                          required
                          @input="$v.password.$touch()"
                          @blur="$v.password.$touch()"
                          @click:append="showPassword = !showPassword"
                      ></v-text-field>
                    </v-col>
                    <v-col>
                    <h2>Confirm Password</h2>
                         <v-text-field
                            v-model="confirmPassword"
                            :error-messages="confirmPasswordErrors"
                            :append-icon="showPassword1 ? 'mdi-eye' : 'mdi-eye-off'"
                            :rules="[rules.required]"
                            :type="showPassword1 ? 'text' : 'password'"
                            name="input-10-2"
                            placeholder="Confirm your password here"
                            required
                            counter
                            @input="$v.confirmPassword.$touch()"
                            @blur="$v.confirmPassword.$touch()"
                            @click:append="showPassword1 = !showPassword1"
                        ></v-text-field>
                    </v-col>

                    <v-divider></v-divider>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn to="/">
                            log-in
                        </v-btn>
                        <v-spacer></v-spacer>

                        <v-btn 
                        color="#FF9062" 
                        @click="Checksignup"
                        >
                            Sign-up
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
        userName: { required, maxLength: maxLength(30) },
        EmailAddress: { required, email },
        password: { required },
        confirmPassword: { required },        
    },
    data: () => ({
        userName : '',
        EmailAddress : '',
        password : '',
        confirmPassword: '',
        showPassword : false ,
        showPassword1 : false ,
        snackbar : false ,
        message : '',
        rules: {
            required: value => !!value || 'Required.',
            min: v => v.length >= 8 || 'Min 8 characters',
            emailMatch: () => (`The email and password you entered don't match`),
        },
    }),
    computed: {
        nameErrors() {
        const errors = [];
        if (!this.$v.userName.$dirty) return errors;
        !this.$v.userName.maxLength &&
            errors.push("Name must be at most 30 characters long");
        !this.$v.userName.required && errors.push("Name is required.");
        return errors;
        },
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
        },
        confirmPasswordErrors() {
        const errors = [];
        if (!this.$v.confirmPassword.$dirty) return errors;
        !this.$v.confirmPassword.required && errors.push("Confirm Password is required");
        if (this.confirmPassword !== this.password) errors.push("The two passwords that you entered do not match!");
        return errors;
        }

    },
    methods : {
        Checksignup : function(){
            axios.get('http://localhost:8080/api/signUp',{
                params: {
                    emailaddress : this.EmailAddress,
                    password : this.password,
                    userName : this.userName,
                }
            }).then(Response=>{
                const Data = Response.data;
                this.message = Data ;
            });
            this.snackbar = true ;
        }
    }
}
</script>

<style>

</style>