<template>
    <div class="login-container">
      <div class="login-box">
        <h1 class="login-title">Sign up</h1>
  
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
          </div>
  
          <div class="center">
            <v-btn color="#3498db" @click="Checksignup" style="color: white;">Sign up</v-btn>
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
    margin-bottom: 10px;
  }
  
  .form-group {
    margin: 0px;
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
    margin-top: 10px;
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
