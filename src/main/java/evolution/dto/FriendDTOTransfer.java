package evolution.dto;

import evolution.dto.model.FriendDTO;
import evolution.dto.model.FriendDTOFull;
import evolution.dto.model.UserDTO;
import evolution.model.Friend;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendDTOTransfer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDTOTransfer userDTOTransfer;

    private final ModelMapper modelMapper;

    @Autowired
    public FriendDTOTransfer(UserDTOTransfer userDTOTransfer, ModelMapper modelMapper) {
        this.userDTOTransfer = userDTOTransfer;
        this.modelMapper = modelMapper;
    }

    public FriendDTO modelToDTO(Friend friend) {
        UserDTO first = userDTOTransfer.modelToDTO(friend.getPk().getFirst());
        UserDTO second = userDTOTransfer.modelToDTO(friend.getPk().getSecond());
        return new FriendDTO(first, second, friend.getStatus());
    }

    public FriendDTO modelToDTO(Friend friend, Long auth) {
        UserDTO first;
        UserDTO second;
        if (auth.equals(friend.getPk().getFirst().getId())) {
            first = userDTOTransfer.modelToDTO(friend.getPk().getFirst());
            second = userDTOTransfer.modelToDTO(friend.getPk().getSecond());
        } else {
            second = userDTOTransfer.modelToDTO(friend.getPk().getFirst());
            first = userDTOTransfer.modelToDTO(friend.getPk().getSecond());
        }

        return new FriendDTO(first, second, friend.getStatus());
    }

    public FriendDTOFull modelToDTOFull(Friend friend) {
        FriendDTOFull f = modelMapper.map(friend, FriendDTOFull.class);
        f.setActionUser(userDTOTransfer.modelToDTO(friend.getActionUser()));
        return f;
    }
}