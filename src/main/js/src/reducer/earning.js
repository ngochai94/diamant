const earning = (state = 0, action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.earning;
    default:
      return state
  }
};

export default earning;
