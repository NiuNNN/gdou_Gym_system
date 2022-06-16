package com.gdou.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdou.game.domain.Game;

public interface IGameService extends IService<Game>{
    boolean updateGame(Game game);
}
