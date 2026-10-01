// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Guanxi is ERC20, Ownable {
    // Mints 1,000,001 GXI to the contract deployer upon creation
    constructor(
        uint256 initialSupply
    ) ERC20("Guanxi", "GXI") Ownable(msg.sender) {
        _mint(msg.sender, initialSupply * 10 ** decimals());
    }

    // Allows the owner to mint additional tokens if needed
    function mint(address to, uint256 amount) public onlyOwner {
        _mint(to, amount * 10 ** decimals());
    }
}
