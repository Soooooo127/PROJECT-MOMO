var foundId = [[${member.memberid}]];


function masking(name) {
  if (name.length === 1) {
      return name;
  } else if (name.length === 2) {
      return name.slice(0, 1) + '*';
  } else {
      return name.slice(0, 1) + '*'.repeat(name.length - 2) + name.slice(-1)
  }
}