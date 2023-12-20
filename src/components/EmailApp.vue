<template>
    <div>
        <SendEmail/>
        <ViewEmail/>
    </div>
</template>

<script>
    import SendEmail from './SendEmail.vue';
    import ViewEmail from './ViewEmail.vue';


    export default {
    name: 'EmailApp',
    components: { 
        SendEmail,
       ViewEmail,
    },
    
}
</script>

<style scoped>

</style>
  