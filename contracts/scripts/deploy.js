const hre = require("hardhat");

async function main() {
  const [deployer] = await hre.ethers.getSigners();

  // 1. Verify deployer exists
  if (!deployer || !deployer.address) {
    throw new Error("No deployer account found!");
  }

  // 2. Verify deployer address and ETH balance
  const balance = await hre.ethers.provider.getBalance(deployer.address);
  console.log("-----------------------------------------------");
  console.log("Deployer Address:", deployer.address);
  console.log("Deployer ETH Balance:", hre.ethers.formatEther(balance), "ETH");
  console.log("-----------------------------------------------");

  // 3. Deploy Guanxi Token
  const initialSupply = 1000001; // 1,000,001 GXI
  const Guanxi = await hre.ethers.getContractFactory("Guanxi");
  const guanxi = await Guanxi.deploy(initialSupply);

  await guanxi.waitForDeployment();
  const tokenAddress = await guanxi.getAddress();

  console.log("Guanxi (GXI) successfully deployed!");
  console.log("Contract Address:", tokenAddress);
  console.log("-----------------------------------------------");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});