<template>

    <button class="ViewEmail" @click="openWindow">View Email</button>
    <div id="ViewE" class="formC" ref="formC">
    <form class="formC-content animate">
        <div class="container">
        <button class="closebtn" @click="closeWindow">X</button>
        <p>View Email</p>
        <div class="info">
            <span class="time">{{ time }}</span>
            <span class="priority">{{ priority }}</span>
        </div>
        <div class="ebtns">
            <button id="move" class="sbt" >move</button>
            <button id="delete" class="sbt" >Delete</button>
        </div>
        <div class="SendInfo">
            <label for="subject">Subject</label>
            <input id="subject" class="text" type="text" :value="subject" readonly>
            <br>
            <label for="sendTo">From</label>
            <input id="sendTo" class="text" type="email" :value="sender" readonly>
        </div>
        <label for="email">Email text</label>
        <span id="email" class="text2" type="text">{{ message }}</span>
        </div>
    </form>
    </div>

</template>

<script>
    export default {
        data() {
        return {
            subject: 'Email from Deif',
            sender: 'deifo_with_knife@murders.com',
            message: `
                Indoor Obstacle Course:
                Create a makeshift obstacle course using household items like chairs, cushions, and small boxes. Guide your dog through the course, encouraging them to jump over obstacles, weave between objects, and navigate tunnels. This not only engages them physically but also provides mental stimulation.

                Tug of War:
                A classic game of tug of war can be a great indoor activity. Use a sturdy rope or a tug toy and engage in a friendly tug of war with your dog. This is an excellent way to release pent-up energy and also strengthens the bond between you and your furry friend.

                Interactive Puzzle Toys:
                Invest in or create interactive puzzle toys for your dog. These toys often dispense treats when manipulated correctly. They provide mental stimulation as your dog works to figure out how to access the treats. It's a great way to keep them entertained and focused.

                Hide and Seek:
                Play a game of hide and seek with your dog. Start by hiding behind furniture or in another room, then call your dog and let them find you. You can gradually increase the difficulty by hiding in more challenging spots. This game not only provides physical exercise but also stimulates their sense of smell and problem-solving skills.

                DIY Snuffle Mat:
                Create a DIY snuffle mat by tying strips of fabric through a rubber mat or using a rubber mat with holes. Sprinkle your dog's favorite treats or kibble among the strips, and let them use their nose to search for the goodies. This engages their sense of smell and provides a mentally stimulating and rewarding activity.

            Remember to tailor these activities to your dog's abilities and preferences. Always prioritize their safety and monitor them during playtime. If your dog has a lot of energy, a combination of physical and mental stimulation will help keep them happy and satisfied.
            `,
            time: '12/20/2023 7:04 am',
            priority: 'Ergent',
        };
    },
    mounted() {
       
    },
    methods: {
        openWindow(){
            this.$refs.formC.style.display = 'block';

        },
        closeWindow(e){
            this.$refs.formC.style.display = 'none';
            e.preventDefault();
        },
    },
    
}
</script>

<style scoped>

.text, .email, .text2{
    padding: 5px 15px;
    margin: 8px 0;
    display: inline-block;
    box-sizing: border-box;
    border-radius: 4px;
    overflow: auto;

}

.closebtn{
    text-align: center;
    margin-left: 97%;
    margin-right: 0;
    width: auto;
}
.info{
    color: #BBE1FA;
}
.time{
    display: block;
    margin-left: 0;
    margin-top: 3px;
}
.priority{
    display: block;
    margin-left: 0;
    margin-top: 3px;
}
.container {
    padding: 16px;

}

.container p{
    margin: auto ;
    font-size: 48px;
    font-weight: 700;
    line-height: 58px;
    letter-spacing: 0em;
    text-align: center;
    color: #BBE1FA;
}

label {
    display: inline-block;
    width: 10%;
    margin-top: 8px;
    color: #BBE1FA;


}
.text{
    width: 90%;
   /* min-height: 30px;
    max-height: 60px;*/
    height: 30px;
    border-radius: 5px;
    /*box-shadow: 0 2px 0px rgba(0, 0, 0, 0.7);*/
    background-color: #D1D4C9;
}
.text2{
    width: 100%;
    min-height: 250px;
    max-height: 250px;
    min-width: 100%;
    max-width: 100%;
    
    border-radius: 5px;
    /*box-shadow: 0 2px 0px rgba(0, 0, 0, 0.7);*/
    background-color: #D1D4C9;
}
.ebtns{
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 5px;
    grid-auto-flow: row;
    margin-left: auto; 
    margin-right: 0;
    width:150px;
}

.sbt{
    background-color: #0F4C75;
    color: #FBF7EB;
    border-radius: 7px;
    cursor: pointer;
    width: auto;
    height: 42px;
    padding: 0 12px ;
}


.sendInfo{
    display: grid;
    column-gap: 50px;
    row-gap: 50px;

}
.formC {
    display: none; 
    position: fixed;
    left: 0;
    top: 0;
    width: 100%; 
    height: 100%; 
    background-color: rgba(0,0,0,0.4); 
    padding-top: 0px;
    z-index: 6;
}

.formC-content{
    border-radius: 5px; 
}
.formC-content{
    margin: 3% auto 5% auto; 
    background-color: #1B262C;
    width: 800px; 
    height: auto;
}

.animate {
  -webkit-animation: animatezoom 0.6s;
  animation: animatezoom 0.6s
}

@-webkit-keyframes animatezoom {
  from {-webkit-transform: scale(0)} 
  to {-webkit-transform: scale(1)}
}
  
@keyframes animatezoom {
  from {transform: scale(0)} 
  to {transform: scale(1)}
}

</style>
  