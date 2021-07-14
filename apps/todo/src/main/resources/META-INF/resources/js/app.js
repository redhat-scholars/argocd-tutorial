/*global Vue, todoStorage */
(function (exports) {

    'use strict';

    var filters = {
        all: function (todos) {
            return todos;
        },
        active: function (todos) {
            return todos.filter(function (todo) {
                return !todo.completed;
            });
        },
        completed: function (todos) {
            return todos.filter(function (todo) {
                return todo.completed;
            });
        }
    };

    exports.app = new Vue({

        // the root element that will be compiled
        el: '.todoapp',

        // app initial state
        data: {
            todos: [],
            newTodo: '',
            editedTodo: null,
            visibility: 'all',
            currentCloud: '',
        },

        computed: {
            filteredTodos: function () {
                return filters[this.visibility](this.todos);
            },
            remaining: function () {
                return filters.active(this.todos).length;
            },
            allDone: {
                get: function () {
                    return this.remaining === 0;
                },
                set: function (value) {
                    this.todos.forEach(function (todo) {
                        todo.completed = value;
                    });
                }
            }
        },

        methods: {

            pluralize: function (word, count) {
                return word + (count === 1 ? '' : 's');
            },

            addTodo: async function () {
                var value = this.newTodo && this.newTodo.trim();
                if (!value) {
                    return;
                }

                const item = await todoStorage.add({
                    title : value,
                    order: this.todos.length + 1,
                    completed: false
                });
                this.todos.push(item);
                this.currentCloud = await todoStorage.fetchcloud()
                this.newTodo = '';
            },

            removeTodo: async function (todo) {
                await todoStorage.delete(todo);
                await this.reload();
            },

            toggleTodo: function (todo) {
                todo.completed = ! todo.completed;
                todoStorage.save(todo);
            },

            editTodo: async function (todo) {
                this.beforeEditCache = todo.title;
                this.editedTodo = todo;
            },

            doneEdit: function (todo) {
                if (!this.editedTodo) {
                    return;
                }
                this.editedTodo = null;
                todo.title = todo.title.trim();
                if (!todo.title) {
                    this.removeTodo(todo);
                } else {
                    todoStorage.save(todo);
                }
            },

            cancelEdit: function (todo) {
                this.editedTodo = null;
                todo.title = this.beforeEditCache;
            },

            removeCompleted: async function () {
                await todoStorage.deleteCompleted();
                await this.reload();
            },

            reload: async function () {
                const data = await todoStorage.fetch();
                app.todos = data;
            }
        },

        // a custom directive to wait for the DOM to be updated
        // before focusing on the input field.
        // http://vuejs.org/guide/custom-directive.html
        directives: {
            'todo-focus': function (el, binding) {
                if (binding.value) {
                    el.focus();
                }
            }
        },

        mounted : async function() {
            this.reload();
        }
    });

})(window);