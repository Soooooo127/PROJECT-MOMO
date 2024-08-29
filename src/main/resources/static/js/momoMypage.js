var foundid = document.querySelector('#foundId')
var foundnick = document.getElementById("#foundname")
var foundnick = document.getElementById("#foundnick")
var foundemail = document.getElementById("#foundEmail")


function masking(name) {
  if (name.length === 1) {
      return name;
  } else if (name.length === 2) {
      return name.slice(0, 1) + '*';
  } else {
      return name.slice(0, 1) + '*'.repeat(name.length - 2) + name.slice(-1)
  }
}