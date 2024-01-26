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
          <label for="username">Username</label>
          <v-text-field
            v-model="username"
            :error-messages="usernameErrors"
            required
            counter
            :rules="[rules.required, rules.minU, rules.maxU]"
            placeholder="Enter your username"
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
import { required, email, minLength, maxLength } from 'vuelidate/lib/validators';

Vue.use(VueAxios, axios);

export default {
  mixins: [validationMixin],
  validations: {
    username: { required , minLength: minLength(3), maxLength: maxLength(30)},
    EmailAddress: { required, email },
    password: { required, minLength: minLength(8) },
  },
  data: () => ({
    username: '',
    EmailAddress: '',
    password: '',
    showPassword: false,
    snackbar: false,
    message: '',
    rules: {
      required: value => !!value || 'Required.',
      minU: v => v.length >= 3 || 'Min 3 characters',
      maxU: v => v.length <= 30 || 'Max 30 characters',
      min: v => v.length >= 8 || 'Min 8 characters',
      emailMatch: () => `The email and password you entered don't match`,
    },
  }),
  methods: {
    CheckLogin: function () {
      axios.post('http://localhost:8080/login', {
        username: this.username,
        password: this.password,
      }).then((Response => Response.data))
        .then((data) => {
          console.log(data);
          if (data === '') {
            this.message = 'The username and password you entered don\'t match';
            this.snackbar = true;
          } else {
            localStorage.setItem('token', data);
            this.message = 'Successfully Log in';
            this.snackbar = true;
            setTimeout(this.gomainpage, 1);
          }
        });
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
    usernameErrors() {
      const errors = [];
      if (!this.$v.username.$dirty) return errors;
      !this.$v.username.required && errors.push('Username is required');
      !this.$v.username.minLength && errors.push('Username must be at least 3 characters long');
      !this.$v.username.maxLength && errors.push('Username must be at most 30 characters long');
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