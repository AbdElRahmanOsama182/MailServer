<template>
    <div class="panel" :key="componentKey">
        <div class="folder" v-for="folder in folders" :key="folder.id" >
            <div @click="viewFolder(folder)" class="foldername">{{ folder.name }}</div>
            <button><i @click="renameFolderPanel(folder)" class="icon mdi mdi-pencil"></i></button>
            <button><i @click="deleteFolder(folder)" class="icon mdi mdi-trash-can-outline"></i></button>
        </div>
        <div v-if="add" class="addFolderPanel">
            <input  type="text" v-model="addedFolderName" style="color: white;" />
            <button @click="addFolder"> Add </button>
        </div>
        <div v-if="rename" class="addFolderPanel">
            <input  type="text" v-model="renamedFolderName" style="color: white;" />
            <button @click="renameFolder(renamedFolder,renamedFolderName)"> Rename </button>
        </div>
        <button class="folder" @click="addFolderPanel">Add Folder</button>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    props: ['folders'],
    methods: {
        remount() {
            this.componentKey += 1;
        },
        addFolderPanel() {
            this.rename = false;
            this.add = true;
        },
        renameFolderPanel(folder) {
            this.add = false;
            this.rename = true;
            this.renamedFolderName = folder.name;
            this.renamedFolder = folder;
        },
        async viewFolder(folder) {
            console.log('viewing folder1', folder);
            try {
            const response = await axios.get(`http://localhost:8080/folders/${folder.id}/emails`, {
                headers: {
                    authorization: `${localStorage.getItem('token')}`,
                },
            });
            this.emails = response.data.emails;
            this.pages = response.data.pages;
            } catch (error) {
            console.error('Error fetching emails', error);
            }
            console.log('viewing folder2', folder);
            this.$emit('view-folder', { folder, emails: this.emails, pages: this.pages });
            console.log('viewing folder3', folder);
        },
        async addFolder() {
            this.add = false;
            try {
                const response = await axios.post('http://localhost:8080/folders', {
                name: this.addedFolderName,
                },{
                    headers: {
                        authorization: `${localStorage.getItem('token')}`,
                    },
                    
                })
                this.folders.push(response.data);
                this.addedFolderName = '';
            } catch (error) {
                console.error('Error adding folder', error);
            }
            this.remount();
            },
        async renameFolder(folder, newName) {
            this.rename = false;
            try {
            const response = await axios.put(`http://localhost:8080/folders/${folder.id}`, {
                name: newName,
            }, {
                headers: {
                    authorization: `${localStorage.getItem('token')}`,
                },
            });
            const index = this.folders.findIndex(f => f.id === folder.id);
            this.folders[index] = response.data;
            } catch (error) {
            console.error('Error renaming folder', error);
            }
            this.remount();
        },
        async deleteFolder(folder) {
            try {
            await axios.delete(`http://localhost:8080/folders/${folder.id}`, {
                headers: {
                    authorization: `${localStorage.getItem('token')}`,
                },
            });
            const index = this.folders.findIndex(f => f.id === folder.id);
            this.folders.splice(index, 1);
            } catch (error) {
            console.error('Error deleting folder', error);
            }
            this.remount();
        },
        },
    data() {
        return {
            // other data properties...
            emails: [],
            add: false,
            rename: false,
            renamedFolder: null,
            addedFolderName: '',
            renamedFolderName: '',
            componentKey: 0,
            pages: 0,
        };
    },
};
</script>

<style scoped>

.panel {
    background-color: #071551; 
    display: grid;
    justify-content: center;
    align-items: center;
    padding: 20px;
    border: 1px solid white;
    border-radius: 10px;
    gap: 10px;
    transform: translate(0, -50%);
}
.folder {
    border: 1px solid white;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 5px;
    color: white;
    width: 300px;
    height: 50px;
    padding: 10px;
}

.folder:hover, button:hover {
    background-color: white;
    color: black;
    cursor: pointer;
}

.foldername {
    display: inline-block;
    width: 80%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.icon {
    font-size: 20px;
}

.addFolderPanel {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
}

.addFolderPanel input {
    width: 200px;
    height: 30px;
    border-radius: 5px;
    border: 1px solid white;
    padding: 5px;
}

.addFolderPanel button {
    width: 65px;
    height: 30px;
    border-radius: 5px;
    border: 1px solid white;
    background-color: white;
    color: black;
    cursor: pointer;
}

</style>