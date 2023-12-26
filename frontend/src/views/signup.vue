<template>
    <div class="login-container">
      <div class="login-box">
        <h1 class="login-title">SIGN-UP</h1>
  
        <form @submit.prevent="Checksignup">
            <div v-show="snackbar" class="snackbar">
          {{ message }}
          <button class="btn close-btn" @click="snackbar = false">Close</button>
        </div>
          <div class="form-group">
            <label for="name">Name</label>
            <v-text-field
              v-model="name"
              placeholder="Enter your name"
              :error-messages="nameErrors"
              :counter="30"
              required
              @input="$v.name.$touch()"
              @blur="$v.name.$touch()"
              outlined
            ></v-text-field>
          </div>
          <div class="form-group">
            <label for="username">Username</label>
            <v-text-field
              v-model="userName"
              placeholder="Enter your username"
              :error-messages="userNameErrors"
              :counter="30"
              required
              @input="$v.userName.$touch()"
              @blur="$v.userName.$touch()"
              outlined
            ></v-text-field>
          </div>
  
          <div class="form-group">
            <label for="email">E-mail</label>
            <v-text-field
              v-model="EmailAddress"
              :error-messages="emailErrors"
              placeholder="Enter your email"
              required
              @input="$v.EmailAddress.$touch()"
              @blur="$v.EmailAddress.$touch()"
              outlined
            ></v-text-field>
            <!-- <div class="error-message" v-if="$v.EmailAddress.$dirty && !$v.EmailAddress.email">
              Must be a valid email
            </div>
            <div class="error-message" v-if="$v.EmailAddress.$dirty && !$v.EmailAddress.required">
              E-mail is required
            </div> -->
          </div>
  
          <div class="form-group">
            <label for="password">Password</label>
            <v-text-field
              v-model="password"
              :error-messages="passwordErrors"
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              :rules="[rules.required, rules.min]"
              :type="showPassword ? 'text' : 'password'"
              name="input-10-1"
              placeholder="Enter your password"
              hint="At least 8 characters"
              counter
              required
              @input="$v.password.$touch()"
              @blur="$v.password.$touch()"
              @click:append="showPassword = !showPassword"
              outlined
            ></v-text-field>
          </div>
  
          <div class="form-group">
            <label for="confirm-password">Confirm Password</label>
            <v-text-field
              v-model="confirmPassword"
              :error-messages="confirmPasswordErrors"
              :append-icon="showPassword1 ? 'mdi-eye' : 'mdi-eye-off'"
              :rules="[rules.required]"
              :type="showPassword1 ? 'text' : 'password'"
              name="input-10-2"
              placeholder="Confirm your password"
              required
              counter
              @input="$v.confirmPassword.$touch()"
              @blur="$v.confirmPassword.$touch()"
              @click:append="showPassword1 = !showPassword1"
              outlined
            ></v-text-field>
            <!-- <div class="error-message" v-if="$v.confirmPassword.$dirty && !$v.confirmPassword.required">
              Confirm Password is required
            </div>
            <div class="error-message" v-if="confirmPassword !== password">
              The two passwords do not match
            </div> -->
          </div>
  
          <div class="center">
            <v-btn color="#3498db" @click="Checksignup" style="color: white;">Sign-up</v-btn>
          </div>
        </form>
  
        <div class="bottom-text">
          <p>Already have an account? <router-link to="/">Log In</router-link></p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Vue from 'vue';
  import axios from 'axios';
  import VueAxios from 'vue-axios';
  import { validationMixin } from 'vuelidate';
  import { required, email, minLength, maxLength } from 'vuelidate/lib/validators';
  
  Vue.use(VueAxios, axios);
  
  export default {
    mixins: [validationMixin],
    validations: {
      name: { required, maxLength: maxLength(30), minLength: minLength(3) },
      userName: { required, maxLength: maxLength(30), minLength: minLength(3) },
      EmailAddress: { required, email },
      password: { required, minLength: minLength(8) },
      confirmPassword: { required },
    },
    data: () => ({
      name: '',
      userName: '',
      EmailAddress: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
      showPassword1: false,
      snackbar: false,
      message: '',
      rules: {
        required: value => !!value || 'Required.',
        min: v => v.length >= 8 || 'Min 8 characters',
        minU: v => v.length >= 3 || 'Min 3 characters',
        maxU: v => v.length <= 30 || 'Max 30 characters',
        emailMatch: () => `The email and password you entered don't match`,
      },
    }),
    computed: {
      nameErrors() {
        const errors = [];
        if (!this.$v.name.$dirty) return errors;
        !this.$v.name.minLength && errors.push("Name must be at least 3 characters long");
        !this.$v.name.maxLength && errors.push("Name must be at most 30 characters long");
        !this.$v.name.required && errors.push("Name is required.");
        return errors;
      },
      userNameErrors() {
        const errors = [];
        if (!this.$v.userName.$dirty) return errors;
        !this.$v.userName.minLength && errors.push("Username must be at least 3 characters long");
        !this.$v.userName.maxLength && errors.push("Username must be at most 30 characters long");
        !this.$v.userName.required && errors.push("Username is required.");
        return errors;
      },
      emailErrors() {
        const errors = [];
        if (!this.$v.EmailAddress.$dirty) return errors;
        !this.$v.EmailAddress.email && errors.push("Please enter your email address using the format name@example.com");
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
        if (this.confirmPassword !== this.password) errors.push("The two passwords do not match!");
        return errors;
      },
    },
    methods: {
      Checksignup: function () {
        if (this.$v.$invalid) {
          // If the form is invalid, display an error message
          this.message = 'Please fill in all fields correctly';
          this.snackbar = true;
        } else {
          axios.post('http://localhost:8080/register', {
            name: this.name,
            username: this.userName,
            email: this.EmailAddress,
            password: this.password,
          }).then((response) => {
            if (response.data === '') {
              this.message = 'User already exists';
              this.snackbar = true;
            } else {
              this.message = 'User created successfully';
              this.snackbar = true;
              this.$router.push('/');
            }
          });
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f2f2f2;
  }
  
  .login-box {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    max-width: 400px;
    width: 100%;
  }
  
  .login-title {
    font-size: 24px;
    text-align: center;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin: 0;
  }
  
  label {
    display: block;
    font-size: 14px;
    margin-bottom: 5px;
  }
  
  .btn {
    padding: 10px 15px;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .bottom-text {
    text-align: center;
    margin-top: 20px;
    font-size: 14px;
  }
  
  .bottom-text a {
    color: #3498db;
    text-decoration: none;
  }
  
  .snackbar {
    background-color: #f05e5e;
    border-radius: 4px;
    color: white;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 15px 0;
  }
  
  .close-btn {
    background-color: transparent;
    border-radius: 4px;
    border: 2px solid white;
  }
  
  .center {
    text-align: center;
  }
</style>
  








<!-- <template>
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

<style scoped>

</style> -->