/*jshint unused:false */

(function (exports) {

    'use strict';
    // axios.defaults.baseURL = 'http://todo-backend:8080/';

    var serverUrl = 'api/';

    exports.todoStorage = {
        fetch: async function () {
            const response = await axios.get(serverUrl);
            console.log(response.data);
            return response.data;
        },
        add : async function(item) {
          console.log("Adding todo item " + item.title);
          return (await axios.post(serverUrl, item)).data;
        },
        save: async function (item) {
            console.log("save called with", item);
            await axios.patch(serverUrl + item.id, item);
        },
        delete: async function(item) {
            await axios.delete(serverUrl + item.id);
        },
        deleteCompleted: async function() {
            await axios.delete(serverUrl);
        },
        fetchcloud: async function () {
            const response = await axios.get(serverUrl + "cloud");
            console.log("Fetched Cloud" + response.data);
            return response.data;
        }
    };

})(window);
