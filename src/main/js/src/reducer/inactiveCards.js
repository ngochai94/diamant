const inactiveCards = (state = [], action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.inactiveCards;
    default:
      return state
  }
};

export default inactiveCards;
