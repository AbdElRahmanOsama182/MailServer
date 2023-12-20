<template>

    <button class="SendEmail" onclick="document.getElementById('SendE').style.display='block'">Send Email</button>
    <div id="SendE" class="formC">
    <form class="formC-content animate">
        <div class="container">
        <p>Send Email</p>
        <div class="SendInfo">
            <label for="subject">Subject</label>
            <input id="subject" class="text" type="text">
            <br>
            <label for="sendTo">Send to</label>
            <input id="sendTo" class="text" type="email">
        </div>
        <label for="email">Email text</label>
        <textarea id="email" class="text2" type="text"></textarea>
        <div class="abtns">
            <button id="attach" class="attach">Attachment</button>
            <select id="Priority" class="pr">
                <option value="important">Ergent</option>
                <option value="high">High</option>
                <option value="med">Medium</option>
                <option value="low">Low</option>
            </select>
        </div>
        <div class="ebtns">
            <button id="send" class="sbt" type="submit">Send</button>
            <button id="draft" class="sbt" type="submit">Draft</button>
        </div>
        </div>
    </form>
    </div>

</template>

<script>
    export default {
        data() {
        return {
        };
    },
    mounted() {
        var formC = document.getElementById('SendE');
        window.onclick = function(event) {
            if (event.target == formC) {
                formC.style.display = "none";
            }

        } 
    },
    methods: {
        
    },
    
}
</script>

<style scoped>
    input[type=text], input[type=email], textarea{
    padding: 12px 15px;
    margin: 8px 0;
    display: inline-block;
    box-sizing: border-box;
    border-radius: 4px;
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
    margin-top: 15px;
    color: #BBE1FA;


}
.text{
    width: 90%;
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

.ebtns, .abtns{
    display: flex;
    justify-content: space-between;
    height: 42px;
    font-size: 16px;
    font-weight: 300;
    line-height: 19px;
    letter-spacing: 0em;
    text-align: center;
}

.ebtns{
    margin-left: auto; 
    margin-right: 0;
    width: 130px;
}
.abtns{
    margin-left: 0; 
    margin-right: auto;
    width:150px;
}
.pr{
    margin-left: 5px;
}
.sbt, .attach, .pr{
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
    margin: 5% auto 15% auto; 
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
  