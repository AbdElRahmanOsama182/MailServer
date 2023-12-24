<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="login-title">Welcome Back!</h1>

      <form @submit.prevent="CheckLogin">
        <div v-show="snackbar" class="snackbar">
          {{ message }}
          <button class="btn close-btn" @click="snackbar = false">Close</button>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <v-text-field
            v-model="EmailAddress"
            :error-messages="emailErrors"
            required
            placeholder="Enter your email"
            @input="$v.EmailAddress.$touch()"
            @blur="$v.EmailAddress.$touch()"
            outlined
          ></v-text-field>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <v-text-field
            v-model="password"
            :error-messages="passwordErrors"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.min]"
            :type="showPassword ? 'text' : 'password'"
            hint="At least 8 characters"
            counter
            required
            placeholder="Enter your password"
            @input="$v.password.$touch()"
            @blur="$v.password.$touch()"
            @click:append="showPassword = !showPassword"
            outlined
          ></v-text-field>
        </div>
        <div class="center"><button class="btn login-btn" @click="CheckLogin">Log In</button></div>
      </form>

      <div class="bottom-text">
        <p> Don't have an account? <router-link to="/signup">Sign Up</router-link></p>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import { validationMixin } from 'vuelidate';
import { required, email, minLength } from 'vuelidate/lib/validators';

Vue.use(VueAxios, axios);

export default {
  mixins: [validationMixin],
  validations: {
    EmailAddress: { required, email },
    password: { required, minLength: minLength(8) },
  },
  data: () => ({
    EmailAddress: '',
    password: '',
    showPassword: false,
    snackbar: false,
    message: '',
    rules: {
      required: value => !!value || 'Required.',
      min: v => v.length >= 8 || 'Min 8 characters',
      emailMatch: () => `The email and password you entered don't match`,
    },
  }),
  methods: {
    CheckLogin: function () {
      this.gomainpage();
      if (this.$v.$invalid) {
        // If the form is invalid, display an error message
        this.message = 'Please fix the above errors.';
        this.snackbar = true;
      } else {
        // Your login logic here
        this.snackbar = true;
        this.gomainpage();
      }
    },
    gomainpage() {
      this.$router.push({ name: 'mainpage' });
    },
  },
  computed: {
    emailErrors() {
      const errors = [];
      if (!this.$v.EmailAddress.$dirty) return errors;
      !this.$v.EmailAddress.email && errors.push('Please enter your email address using the format name@example.com');
      !this.$v.EmailAddress.required && errors.push('E-mail is required');
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.required && errors.push('Password is required');
      return errors;
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
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
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
  
  .login-btn {
    background-color: #3498db;
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
</template> -->
<!--   
  
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
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-size: 14px;
    margin-bottom: 5px;
  }
  
  .form-input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .btn {
    padding: 10px 15px;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .login-btn {
    background-color: #3498db;
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
    background-color: #333;
    color: white;
    padding: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
  }
  
  .close-btn {
    background-color: #3498db;
  }
  
   .error-border {
    border-color: #e74c3c; /* Red color for error */
  }
  
  .error-message {
    color: #e74c3c;
    font-size: 12px;
    margin-top: 5px;
  }
/*   
  .hint-message {
    font-size: 12px;
    color: #999;
    margin-top: 5px;
  } */

</style> -->
  
  
  

<!-- <script>

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
            this.gomainpage();
            // axios.get('http://localhost:8080/api/login',{
            //     params: {
            //         Emailaddress : this.EmailAddress,
            //         password : this.password,
            //     }
            // }).then(Response=>{
            //     const Data = Response.data;
            //     this.message = Data ;
            //     this.snackbar = true ;
            //     if(this.message == "Successfully Log in"){
            //         setTimeout(this.gomainpage, 1000);
            //     }
            // });
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
</script> -->

<!-- <style scoped>
h1,.v-btn,h2{
    font-family: 'JetBrains Mono';
}

.v-btn{
    margin: 10px;
}

</style> -->